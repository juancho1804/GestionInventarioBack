package com.manguerasjc.productservice.services.DTO.request;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemDTO {
    private Long shoppingCartId;
    private Long productVariantId;
    private int quantity;
    private double subtotal;
}
