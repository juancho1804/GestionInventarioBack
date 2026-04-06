package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "shoppingCart")
    private List<Item> items = new ArrayList<>();

    @Column
    private double total;

}
