package com.manguerasjc.productservice.services.DTO.request;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductRequestDTO(
        Long categoryId,
        String color,
        Long brandId,
        double price,
        List<String>existingImages,
        List<MultipartFile> images,
        ProductVariantRequestDTO productVariantRequestDTO
        ) {
}
