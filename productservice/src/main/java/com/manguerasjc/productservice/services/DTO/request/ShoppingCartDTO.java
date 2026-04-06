package com.manguerasjc.productservice.services.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShoppingCartDTO {
    private List<ItemDTO>items;
    private double total;

    public ShoppingCartDTO(){
        this.items = new ArrayList<>();
        this.total = 0;
    }
}
