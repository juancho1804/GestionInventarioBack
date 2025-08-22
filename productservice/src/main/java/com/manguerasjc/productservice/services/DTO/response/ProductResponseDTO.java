package com.manguerasjc.productservice.services.DTO.response;

import java.util.Map;

public record ProductResponseDTO(
        Long id,
        String categoryName,
        String name,
        String color,
        String brand,
        double price,
        String urlImage,
        Map<String,Integer> variantes
) {
}
