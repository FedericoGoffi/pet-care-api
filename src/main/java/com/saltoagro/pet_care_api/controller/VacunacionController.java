package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.saltoagro.pet_care_api.dto.AplicarVacunaRequest;
import com.saltoagro.pet_care_api.dto.VacunacionResponse;
import com.saltoagro.pet_care_api.service.MascotaService;

@RestController
@RequestMapping("/vacunaciones")
public class VacunacionController {

    private final MascotaService mascotaService;

    public VacunacionController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping("/mascotas/{mascotaId}")
    @PreAuthorize("hasAnyRole('USER','VETERINARIO','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void aplicarVacuna(
            @PathVariable Long mascotaId,
            @RequestBody AplicarVacunaRequest request,
            Authentication auth) {

        mascotaService.aplicarVacuna(mascotaId, request, auth.getName());
    }

    @GetMapping("/mascotas/{mascotaId}/vencidas")
    public List<VacunacionResponse> vacunasVencidas(
            @PathVariable Long mascotaId,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.vacunasVencidas(mascotaId, username);
    }

    @GetMapping("/mascotas/{mascotaId}/por-vencer")
    public List<VacunacionResponse> vacunasPorVencer(
            @PathVariable Long mascotaId,
            @RequestParam(defaultValue = "30") int dias,
            Authentication authentication) {

        String username = authentication.getName();
        return mascotaService.vacunasPorVencer(mascotaId, dias, username);
    }
}
