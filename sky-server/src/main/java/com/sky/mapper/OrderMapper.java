package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入訂單數據
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("SELECT * FROM orders WHERE number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("SELECT * FROM orders WHERE id=#{id}")
    Orders getById(Long id);


    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("SELECT COUNT(id) FROM orders WHERE status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订中状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    @Select("SELECT * FROM orders WHERE status = #{status} and order_time < #{orderTime}")
    // SELECT * FROM orders WHERE status = ? and order_time < ? (當前時間 - 15mins )
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根據動態條件統計營業額數據
     * @param map
     * @return
     */
    Double sumByMay(Map map);
}
