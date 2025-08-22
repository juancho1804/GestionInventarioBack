package com.manguerasjc.productservice.services.DTO.request;


import org.springframework.web.multipart.MultipartFile;

public record ProductRequestDTO(
        Long categoryId,
        String color,
        Long brandId,
        double price,
        MultipartFile image,
        ProductVariantRequestDTO productVariantRequestDTO
        ) {
}
