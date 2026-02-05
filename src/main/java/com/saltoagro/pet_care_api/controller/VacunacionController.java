package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.saltoagro.pet_care_api.dto.AplicarVacunaRequest;
import com.saltoagro.pet_care_api.dto.VacunacionResponse;
import com.saltoagro.pet_care_api.service.MascotaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vacunaciones")
public class VacunacionController {

    private final MascotaService mascotaService;

    public VacunacionController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PreAuthorize("hasAnyRole('VETERINARIO','ADMIN')")
    @PostMapping("/mascotas/{mascotaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void aplicarVacuna(
            @PathVariable Long mascotaId,
            @Valid @RequestBody AplicarVacunaRequest request,
            Authentication authentication) {

        mascotaService.aplicarVacuna(
                mascotaId,
                request,
                authentication.getName());
    }

    @GetMapping("/mascotas/{mascotaId}/vencidas")
    public List<VacunacionResponse> vacunasVencidas(
            @PathVariable Long mascotaId,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.obtenerVacunasVencidas(mascotaId, username);
    }

    @GetMapping("/mascotas/{mascotaId}/por-vencer")
    public List<VacunacionResponse> vacunasPorVencer(
            @PathVariable Long mascotaId,
            @RequestParam(defaultValue = "30") int dias,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.obtenerVacunasPorVencer(mascotaId, dias, username);
    }
}
