package com.saltoagro.pet_care_api.security.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.saltoagro.pet_care_api.model.Usuario;
import com.saltoagro.pet_care_api.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UsuarioRepository usuarioRepository;

        public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
                this.usuarioRepository = usuarioRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {

                Usuario usuario = usuarioRepository
                                .findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                return new org.springframework.security.core.userdetails.User(
                                usuario.getUsername(),
                                usuario.getPassword(),
                                usuario.getRoles().stream()
                                                .map(rol -> new SimpleGrantedAuthority(
                                                                "ROLE_" + rol.getNombre()))
                                                .collect(Collectors.toList()));
        }
}