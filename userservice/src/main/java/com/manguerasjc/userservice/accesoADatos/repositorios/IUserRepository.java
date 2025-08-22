package com.manguerasjc.userservice.accesoADatos.repositorios;

import com.manguerasjc.userservice.accesoADatos.entidades.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentificacion(String identificacion);
    Optional<User> findByEmail(String email);
}
