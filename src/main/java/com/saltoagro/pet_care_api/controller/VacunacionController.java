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

    private boolean esPrivilegiado(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_VETERINARIO")
                        || a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping("/mascotas/{mascotaId}")
    @PreAuthorize("hasAnyRole('USER','VETERINARIO','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void aplicarVacuna(
            @PathVariable Long mascotaId,
            @RequestBody AplicarVacunaRequest request,
            Authentication auth) {

        mascotaService.aplicarVacuna(mascotaId, request, auth.getName(), esPrivilegiado(auth));
    }

    @GetMapping("/mascotas/{mascotaId}/vencidas")
    @PreAuthorize("hasAnyRole('USER','VETERINARIO','ADMIN')")
    public List<VacunacionResponse> vacunasVencidas(
            @PathVariable Long mascotaId,
            Authentication auth) {

        return mascotaService.vacunasVencidas(mascotaId, auth.getName(), esPrivilegiado(auth));
    }

    @GetMapping("/mascotas/{mascotaId}/por-vencer")
    @PreAuthorize("hasAnyRole('USER','VETERINARIO','ADMIN')")
    public List<VacunacionResponse> vacunasPorVencer(
            @PathVariable Long mascotaId,
            @RequestParam(defaultValue = "30") int dias,
            Authentication auth) {

        return mascotaService.vacunasPorVencer(mascotaId, dias, auth.getName(), esPrivilegiado(auth));
    }
}