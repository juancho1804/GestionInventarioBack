package com.manguerasjc.userservice.fachada.DTO.request;

import com.manguerasjc.userservice.accesoADatos.entidades.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
public class RegisterRequestDTO {
    private String tipoIdentificacion;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String email;
    private String contrasenia;
}
