package com.manguerasjc.productservice.services.DTO.request;

import lombok.Data;

import java.util.Map;

@Data
public class ProductVariantRequestDTO {
    Map<Long, Integer>variantes;
}
