package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.saltoagro.pet_care_api.dto.MascotaRequest;
import com.saltoagro.pet_care_api.dto.MascotaResponse;
import com.saltoagro.pet_care_api.service.MascotaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public MascotaResponse crear(@RequestBody @Valid MascotaRequest request, Authentication auth) {
        return mascotaService.crear(request, auth.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<MascotaResponse> listar(Authentication auth) {
        return mascotaService.listarDelUsuario(auth.getName());
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAnyAuthority('ROLE_VETERINARIO', 'ROLE_ADMIN')")
    public Page<MascotaResponse> listarTodas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio) {
        return mascotaService.listarTodas(pagina, tamanio);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_VETERINARIO', 'ROLE_ADMIN')")
    public MascotaResponse buscarPorId(@PathVariable Long id, Authentication auth) {
        boolean esPrivilegiado = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_VETERINARIO")
                        || a.getAuthority().equals("ROLE_ADMIN"));
        return mascotaService.obtenerPorId(id, auth.getName(), esPrivilegiado);
    }
}