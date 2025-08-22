package com.manguerasjc.productservice.services.DTO.mapper;

import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ProductMapper {
    public Product toEntity(ProductRequestDTO dto) {

        Product p = new Product();
        p.setColor(dto.color());
        p.setPrice(dto.price());
        return p;
    }

    public ProductResponseDTO toResponseDTO(Product p) {
        Map<String,Integer> variantes=new HashMap<>();
        if(p.getVariants()!=null){
            for(ProductVariant v:p.getVariants()){
                variantes.put(v.getSize().getSize().toString(),v.getStock());
            }
        }


        return new ProductResponseDTO(p.getId(),p.getCategory().getName()
                ,p.getName(), p.getColor(),p.getBrand().getBrand().toString(),
                p.getPrice(),p.getUrlImage(),variantes
                );
    }
}
