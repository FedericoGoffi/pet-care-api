package com.saltoagro.pet_care_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public String panelAdmin() {
        return "Bienvenido ADMIN";
    }
}