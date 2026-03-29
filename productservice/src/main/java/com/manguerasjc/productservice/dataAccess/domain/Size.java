package com.manguerasjc.productservice.dataAccess.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "Tallas")
@AllArgsConstructor
@NoArgsConstructor
public class Size {

    public Size(ESize size){
        this.size = size;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla")
    private ESize size;

}
