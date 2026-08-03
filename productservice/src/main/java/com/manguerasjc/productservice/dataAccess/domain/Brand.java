package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String brand;
}
