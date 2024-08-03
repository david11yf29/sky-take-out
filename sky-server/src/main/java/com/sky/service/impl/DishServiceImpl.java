package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品和對應的口味
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入 1 條數據
        dishMapper.insert(dish);

        // 獲取 INSERT 語句生成的主鍵值 -> DishMapper.xml: <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入 n 條數據
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分頁查詢
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 菜品批量刪除
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public void deteleBatch(List<Long> ids) {

        // 判斷當前菜品是否能夠刪除 -- 是否存在起售中菜品
        for (Long id: ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                // 當前菜品處於起售中, 不能刪除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判斷當前菜品是否能夠刪除 -- 是否被套餐關聯
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            // 當前菜品被套餐關聯了, 無法刪除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

//        // 刪除菜品表中的菜品 --> 以下優化成兩個批量刪除方法
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//
//            // 刪除菜品關聯的口味數據
//            dishFlavorMapper.deleteByDishId(id);
//        }

        // 根據菜品 id 集合批量刪除菜品數據
        // SQL: DELETE FROM dish WHERE id IN (?,?,?)
        dishMapper.deleteByIds(ids);

        // 根據菜品 id 集合批量刪除關聯的口味數據
        // SQL: DELETE FROM dish_flavor WHERE dishId  IN (?,?,?)
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根據 id 查詢菜品和對應的口味數據
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根據 id 查詢查詢菜品數據
        Dish dish = dishMapper.getById(id);

        // 根據菜品 id 查詢口味數據
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        // 將查詢到的數據封裝到 dishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 根據 id 修改菜品和對應的口味數據
     * @param dishDTO
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 修改菜品表基本信息
        dishMapper.update(dish);

        // 刪除原有的口味數據
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 重新插入口味數據
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            // 向口味表插入 n 條數據
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    @Transactional
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }
}

