package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.services.DTO.request.AddItemRequestDTO;
import com.manguerasjc.productservice.services.DTO.request.ItemDTO;
import com.manguerasjc.productservice.services.DTO.request.ShoppingCartDTO;
import com.manguerasjc.productservice.services.usercases.IItemService;
import com.manguerasjc.productservice.services.usercases.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    @Autowired
    private IItemService itemService;
    @Autowired
    private IShoppingCartService shoppingCartService;

    @PostMapping("item")
        public ItemDTO crearItem(@RequestBody ItemDTO itemDTO){

        return itemService.crearItem(itemDTO);
    }
}
