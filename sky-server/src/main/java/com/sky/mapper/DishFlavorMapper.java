package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味數據
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根據菜品 id 刪除對應的口味數據
     * @param dishId
     */
    @Delete("DELETE FROM dish_flavor WHERE dish_id = ${dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根據菜品 id 集合批量刪除對應的口味數據
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根據菜品 id 查詢對應的口味數據
     * @param dishId
     * @return
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
