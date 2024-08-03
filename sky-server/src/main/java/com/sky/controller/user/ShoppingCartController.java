package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "端 - 購物車相關接口 [ShoppingCartController 類上方]")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加購物車
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("端 - 添加購物車 [ShoppingCartController 類方法 add]")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("[Logger] 添加購物車, 商品信息為: {}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看購物車
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("端 - 查看購物車 [ShoppingCartController 類方法 list]")
    public Result<List<ShoppingCart>> list() {
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * 清空購物車
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("端 - 清空購物車 [ShoppingCartController 類方法 clean]")
    public Result clean() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
}
