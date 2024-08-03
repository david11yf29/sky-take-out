package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加購物車
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        // 判斷當前加入購物車的商品是否已經存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
//        Long userId = BaseContext.getCurrentId();
        Long userId = 666L; // 固定 userId = 666
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已經存在了, 只需要將數量加上1
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1); // 把抓回來的 item 數量 + 1
            shoppingCartMapper.updateNumberById(cart); // 存回 DB： update shopping_cart set number = ? where id = ?
        } else {
            // 如果不存在, 需要插入一條購物車數據

            // 判斷本次添加到購物車的是菜品還是套餐
            Long dish_id = shoppingCartDTO.getDishId();
            if (dish_id != null) {
                // 本次添加的是菜品
                Dish dish = dishMapper.getById(dish_id);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 本次添加的肯定是套餐
                Long setmeal_id = shoppingCartDTO.getSetmealId();

                Setmeal setmeal = setmealMapper.getById(setmeal_id);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看購物車
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 獲取當前微信用戶的 id
//        Long userId = BaseContext.getCurrentId();
        Long userId = 666L; // 直接固定 666
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }
}
