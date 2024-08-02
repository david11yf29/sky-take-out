package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopControllder")
@RequestMapping("/user/shop")
@Api(tags = "店舖相關接口 [ShopController 類上方]")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 獲取店舖的營業狀態
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("獲取店舖的營業狀態 [ShopController 類方法 getStatus]")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("[Logger] 獲取店舖的營業狀態(FromUser): {}", status == 1 ? "營業中" : "打烊中");
        return Result.success(status);
    }
}
