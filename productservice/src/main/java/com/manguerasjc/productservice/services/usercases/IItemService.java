package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.services.DTO.request.ItemDTO;

public interface IItemService {
    ItemDTO crearItem(ItemDTO itemDTO);
}
