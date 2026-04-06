package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.services.DTO.request.ItemDTO;
import com.manguerasjc.productservice.services.DTO.request.ShoppingCartDTO;

public interface IShoppingCartService {
    ShoppingCartDTO createShoppingCart();

}
