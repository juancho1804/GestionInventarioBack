package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Item;
import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.dataAccess.domain.ShoppingCart;
import com.manguerasjc.productservice.dataAccess.repositories.IItemRepository;
import com.manguerasjc.productservice.dataAccess.repositories.IProductVariantRepository;
import com.manguerasjc.productservice.dataAccess.repositories.IShoppingCartRepository;
import com.manguerasjc.productservice.services.DTO.request.ItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements IItemService{

    @Autowired
    private IItemRepository itemRepository;

    @Autowired
    private IProductVariantRepository productVariantRepository;

    @Autowired
    private IShoppingCartRepository shoppingCartRepository;

    @Override
    public ItemDTO crearItem(ItemDTO itemDTO) {

        //Buscar si existe el producto
        ProductVariant productVariant  = productVariantRepository.findById(itemDTO.getProductVariantId()).
                orElseThrow(()->new EntityNotFoundException("No se encontro el producto..."));

        //Buscar si existe el carrito
        ShoppingCart shoppingCart = shoppingCartRepository.findById(itemDTO.getShoppingCartId()).
                orElseThrow(()->new EntityNotFoundException("No se encontro el carrito..."));

        Item itemEntity = new Item();
        itemEntity.setProduct(productVariant.getProduct());
        itemEntity.setShoppingCart(shoppingCart);
        itemEntity.setQuantity(itemDTO.getQuantity());

        for(Item item: shoppingCart.getItems()){
            if(item.getProduct().getId().equals(productVariant.getProduct().getId())){
                itemEntity.setQuantity(itemEntity.getQuantity()+item.getQuantity());
                break;
            }
        }

        //Asignar subtotal al item
        itemEntity.setSubtotal(calculateSubtotal(itemEntity.getQuantity(), productVariant.getProduct().getPrice()));


        itemEntity = itemRepository.save(itemEntity);

        return ItemDTO.builder().productVariantId(itemEntity.getProduct().getId()).quantity(itemEntity.getQuantity()).
                subtotal(itemEntity.getSubtotal()).shoppingCartId(itemEntity.getShoppingCart().getId()).build();
    }

    private double calculateSubtotal(int quantity, double price){
        return quantity * price;
    }
}
