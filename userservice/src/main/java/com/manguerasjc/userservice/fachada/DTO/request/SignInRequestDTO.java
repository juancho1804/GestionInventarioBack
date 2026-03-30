package com.manguerasjc.userservice.fachada.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInRequestDTO {
    private String email;
    private String contrasenia;
}
