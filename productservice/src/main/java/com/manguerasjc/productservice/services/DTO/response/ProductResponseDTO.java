package com.manguerasjc.productservice.services.DTO.response;

public record ProductResponseDTO(
        Long id,
        String categoryName,
        String name,
        String size,
        String material,
        String color,
        String brand,
        double price,
        int quantity
) {
}
