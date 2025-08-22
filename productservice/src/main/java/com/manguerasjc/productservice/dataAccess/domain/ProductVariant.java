package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductVariant {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
