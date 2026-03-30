package com.manguerasjc.userservice.controllers;

import com.manguerasjc.userservice.fachada.DTO.request.RegisterRequestDTO;
import com.manguerasjc.userservice.fachada.DTO.request.SignInRequestDTO;
import com.manguerasjc.userservice.fachada.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody SignInRequestDTO dto) {
        String token = userService.signIn(dto);
        return ResponseEntity.ok(token);
    }
}
