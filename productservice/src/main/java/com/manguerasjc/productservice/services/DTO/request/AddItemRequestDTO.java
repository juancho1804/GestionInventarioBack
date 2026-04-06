package com.manguerasjc.productservice.services.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequestDTO {
    private ShoppingCartDTO shoppingCartDTO;
    private ItemDTO itemDTO;
}
