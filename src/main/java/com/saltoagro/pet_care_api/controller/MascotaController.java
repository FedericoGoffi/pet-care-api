package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.saltoagro.pet_care_api.dto.MascotaResponse;
import com.saltoagro.pet_care_api.model.Mascota;
import com.saltoagro.pet_care_api.service.MascotaService;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mascota crear(
            @RequestBody Mascota mascota,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.guardar(mascota, username);
    }

    @GetMapping
    public List<MascotaResponse> listar(Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.obtenerMisMascotas(username);
    }

    @GetMapping("/{id}")
    public MascotaResponse buscarPorId(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.obtenerPorId(id, username);
    }
}
