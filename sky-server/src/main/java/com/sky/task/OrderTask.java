package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定時任務類, 定時處裡訂單狀態
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 處理超時訂單
     */
    @Scheduled(cron = "0 * * * * ?") // 每分鐘觸發一次
//    @Scheduled(cron = "2/5 * * * * ?") // 測試：每五秒觸發一次
    public void processTimeoutOrder() {
        log.info("[Logger] 定時處理超時訂單:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        // SELECT * FROM orders WHERE status = ? and order_time < ? (當前時間 - 15mins )
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("訂單超時, 自動取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 處理一直處於派送中狀態的訂單
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨一點觸發一次
//    @Scheduled(cron = "0/5 * * * * ?") // 測試：每五秒觸發一次
    public void processDeliveryOrder(){
        log.info("[Logger] 定時處理處於派送中的訂單:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusHours(-1);

        // SELECT * FROM orders WHERE status = ? and order_time < ? (當前時間 - 15mins )
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
