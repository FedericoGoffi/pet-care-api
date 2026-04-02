package com.saltoagro.pet_care_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saltoagro.pet_care_api.dto.RegistroRequest;
import com.saltoagro.pet_care_api.model.Rol;
import com.saltoagro.pet_care_api.model.Usuario;
import com.saltoagro.pet_care_api.repository.RolRepository;
import com.saltoagro.pet_care_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegistroRequest request) {

        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("El username '" + request.username() + "' ya está en uso");
        }

        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.getRoles().add(rolUser);

        return usuarioRepository.save(usuario);
    }
}