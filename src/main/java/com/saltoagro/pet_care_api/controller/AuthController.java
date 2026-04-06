package com.saltoagro.pet_care_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.saltoagro.pet_care_api.dto.AuthResponse;
import com.saltoagro.pet_care_api.dto.LoginRequest;
import com.saltoagro.pet_care_api.dto.RegistroRequest;
import com.saltoagro.pet_care_api.security.jwt.JwtService;
import com.saltoagro.pet_care_api.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final UsuarioService usuarioService;

        public AuthController(
                        AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        UsuarioService usuarioService) {

                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
                this.usuarioService = usuarioService;
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(
                        @RequestBody @Valid LoginRequest request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));

                String token = jwtService.generarToken(
                                (org.springframework.security.core.userdetails.User) authentication.getPrincipal());

                return ResponseEntity.ok(new AuthResponse(token));
        }

        @PostMapping("/registro")
        public ResponseEntity<AuthResponse> registro(
                        @RequestBody @Valid RegistroRequest request) {

                usuarioService.registrar(request);

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.username(),
                                                request.password()));

                String token = jwtService.generarToken(
                                (org.springframework.security.core.userdetails.User) authentication.getPrincipal());

                return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
        }
}