package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {

    /**
     * 添加購物車
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
