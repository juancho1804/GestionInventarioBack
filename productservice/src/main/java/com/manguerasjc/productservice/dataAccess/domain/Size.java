package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Sizes")
@Data
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private ESize size;

}
