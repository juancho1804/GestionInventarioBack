package com.manguerasjc.productservice.services.DTO.response;

import java.util.List;
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
        List<String> urlImages,
        Map<String,Integer> variantes
) {
}
