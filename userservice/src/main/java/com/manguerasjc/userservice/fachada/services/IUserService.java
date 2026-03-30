package com.manguerasjc.userservice.fachada.services;

import com.manguerasjc.userservice.accesoADatos.entidades.User;
import com.manguerasjc.userservice.fachada.DTO.request.RegisterRequestDTO;
import com.manguerasjc.userservice.fachada.DTO.request.SignInRequestDTO;

public interface IUserService {
    void register(RegisterRequestDTO registerRequestDTO);
    String signIn(SignInRequestDTO signInRequestDTO);
}
