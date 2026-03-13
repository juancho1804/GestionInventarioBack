package com.manguerasjc.productservice.services.DTO.response;

import java.util.Map;

public record ProductResponseDTO(
        Long id,
        String categoryName,
        Long categoryId,
        String name,
        String color,
        String brand,
        int brandId,
        double price,
        String urlImage,
        Map<String,Integer> variantes
) {
}
