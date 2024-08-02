package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopControllder")
@RequestMapping("/admin/shop")
@Api(tags = "店舖相關接口 [ShopController 類上方]")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 設置店舖的營業狀態
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("設置店舖的營業狀態 [ShopController 類方法 setStatus]")
    public Result setStatus(@PathVariable Integer status) {
        log.info("[Logger] 設置店舖的營業狀態(FromAdmin): {}", status == 1 ? "營業中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * 獲取店舖的營業狀態
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("獲取店舖的營業狀態 [ShopController 類方法 getStatus]")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("[Logger] 獲取店舖的營業狀態(FromAdmin): {}", status == 1 ? "營業中" : "打烊中");
        return Result.success(status);
    }
}
