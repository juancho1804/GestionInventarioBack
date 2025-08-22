package com.manguerasjc.userservice.accesoADatos.repositorios;

import com.manguerasjc.userservice.accesoADatos.entidades.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Long> {
    Optional<TipoIdentificacion> findByName(String name);
}
