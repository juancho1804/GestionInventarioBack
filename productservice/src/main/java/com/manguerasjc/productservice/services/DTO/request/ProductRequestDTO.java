package com.manguerasjc.productservice.services.DTO.request;


public record ProductRequestDTO(
        Long categoryId,
        Long sizeId,
        Long materialId,
        String color,
        Long brandId,
        double price,
        int quantity) {
}
