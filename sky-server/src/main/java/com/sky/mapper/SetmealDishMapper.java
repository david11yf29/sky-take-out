package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根據菜品 id 查詢對應的套餐 id
     * @param dishIds
     * @return
     */
    // SELECT setmeal_id FROM setmeal_dish WHERE dish_id IN (1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
