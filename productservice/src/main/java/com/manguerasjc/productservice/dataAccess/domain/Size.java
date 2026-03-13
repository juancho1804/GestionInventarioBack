package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Tallas")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla")
    private ESize size;

}
