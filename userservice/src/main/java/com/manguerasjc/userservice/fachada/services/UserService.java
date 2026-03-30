package com.manguerasjc.userservice.fachada.services;

import com.manguerasjc.userservice.accesoADatos.entidades.*;
import com.manguerasjc.userservice.accesoADatos.repositorios.IRoleRepository;
import com.manguerasjc.userservice.accesoADatos.repositorios.ITipoIdentificacionRepository;
import com.manguerasjc.userservice.accesoADatos.repositorios.IUserRepository;
import com.manguerasjc.userservice.fachada.DTO.request.RegisterRequestDTO;
import com.manguerasjc.userservice.fachada.DTO.request.SignInRequestDTO;
import com.manguerasjc.userservice.security.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired private IUserRepository userRepository;
    @Autowired private ITipoIdentificacionRepository tipoIdentificacionRepository;
    @Autowired private IRoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;

    @Override
    public void register(RegisterRequestDTO dto) {
        TipoIdentificacion tipo = null;
        try{
            tipo = tipoIdentificacionRepository.findByName(ETipoIdentificacion.valueOf(dto.getTipoIdentificacion())).get();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de identificacion no esta disponible : "+e);
        }

        if (userRepository.existsUserByIdentificacionAndTipoIdentificacion(dto.getIdentificacion(), tipo))
            throw new RuntimeException("La identificación ya está asociada a otra cuenta");

        if (userRepository.existsUserByEmail(dto.getEmail()))
            throw new RuntimeException("El email ya está asociado a otra cuenta");

        // Rol por defecto: ROLE_USER
        Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = User.builder()
                .tipoIdentificacion(tipo)
                .identificacion(dto.getIdentificacion())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .contrasenia(passwordEncoder.encode(dto.getContrasenia())) // 👈 encriptada
                .roles(Set.of(roleUser))
                .build();

        userRepository.save(user);
    }

    @Override
    public String signIn(SignInRequestDTO dto) {
        // Valida credenciales — lanza excepción si son incorrectas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getContrasenia())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("ROLE_USER");

        return jwtUtils.generateToken(user.getEmail(), role, user.getNombres()); // devuelve el token
    }
}
