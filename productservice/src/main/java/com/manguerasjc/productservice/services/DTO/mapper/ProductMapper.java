package com.manguerasjc.productservice.services.DTO.mapper;

import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductRequestDTO dto) {
        Product p = new Product();
        p.setQuantity(dto.quantity());
        p.setColor(dto.color());
        p.setPrice(dto.price());
        return p;
    }

    public ProductResponseDTO toResponseDTO(Product p) {
        return new ProductResponseDTO(p.getId(),p.getCategory().getName()
                ,p.getName(),p.getSize().getSize().toString(),p.getMaterial().getMaterial().toString(),
                p.getColor(),p.getBrand().getBrand().toString(),p.getPrice(),p.getQuantity());
    }
}
