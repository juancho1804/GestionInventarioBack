package com.manguerasjc.userservice.accesoADatos.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_identificacion")
public class TipoIdentificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private ETipoIdentificacion name;
    public TipoIdentificacion() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ETipoIdentificacion getTipoIdentificacion() {
        return name;
    }

    public void setTipoIdentificacion(ETipoIdentificacion tipoIdentificacion) {
        this.name = tipoIdentificacion;
    }

}
