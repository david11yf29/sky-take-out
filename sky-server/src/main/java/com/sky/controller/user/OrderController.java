package com.sky.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "端 - 用戶端訂單相關接口 [OrderController 類上方]")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用戶下單
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("端 - 用戶下單 [OrderController 類方法 submit]")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("[Logger] 用戶下單, 參數為: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("端 - 订单支付 [OrderController 類方法 payment]")
//    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("[Logger] 订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("[Logger] 生成预支付交易单：{}", orderPaymentVO);

        // 跳過微信支付 - 改用等待五秒模擬支付完成
        log.info("[Logger] 等待五秒後自動支付完成！");
        TimeUnit.SECONDS.sleep(5);

        // 模擬支付完成後取得 orders number
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        log.info("[Logger] 支付完成, 訂單號碼: {}", orderNumber);

        // 調用支付完成功能
        log.info("[Logger] 修改訂單狀態, 訂單號碼: {}", orderNumber);
        orderService.paySuccess(orderNumber);

        return Result.success(orderPaymentVO);
    }
}
