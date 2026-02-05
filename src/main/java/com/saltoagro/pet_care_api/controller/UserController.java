package com.saltoagro.pet_care_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/perfil")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String perfil() {
        return "Perfil del usuario autenticado";
    }
}
