package com.manguerasjc.userservice.accesoADatos.repositorios;

import com.manguerasjc.userservice.accesoADatos.entidades.ERole;
import com.manguerasjc.userservice.accesoADatos.entidades.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
