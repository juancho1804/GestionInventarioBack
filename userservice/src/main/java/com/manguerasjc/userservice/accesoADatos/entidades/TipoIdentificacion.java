package com.manguerasjc.userservice.accesoADatos.entidades;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tipos_identificacion")
@Getter
public class TipoIdentificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private ETipoIdentificacion name;
    public TipoIdentificacion() {}

}
