package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.dataAccess.domain.ShoppingCart;
import com.manguerasjc.productservice.dataAccess.repositories.IProductRepository;
import com.manguerasjc.productservice.dataAccess.repositories.IProductVariantRepository;
import com.manguerasjc.productservice.dataAccess.repositories.IShoppingCartRepository;
import com.manguerasjc.productservice.services.DTO.request.ItemDTO;
import com.manguerasjc.productservice.services.DTO.request.ShoppingCartDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    public IShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCartDTO createShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart());
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        return shoppingCartDTO;
    }


    private void calculateShoppingCartTotal(ShoppingCartDTO shoppingCartDTO){
        double total = 0;
        for(ItemDTO itemDTO :shoppingCartDTO.getItems()){
            total += itemDTO.getSubtotal();
        }
        shoppingCartDTO.setTotal(total);
    }

    private double calculateSubtotal(int quantity, double price){
        return quantity * price;
    }
}
