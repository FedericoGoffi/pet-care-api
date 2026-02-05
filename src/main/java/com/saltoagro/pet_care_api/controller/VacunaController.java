package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltoagro.pet_care_api.model.Vacuna;
import com.saltoagro.pet_care_api.service.VacunaService;

@RestController
@RequestMapping("/vacunas")
public class VacunaController {

    private final VacunaService vacunaService;

    public VacunaController(VacunaService vacunaService) {
        this.vacunaService = vacunaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Vacuna> listarVacunas() {
        return vacunaService.obtenerTodas();
    }
}