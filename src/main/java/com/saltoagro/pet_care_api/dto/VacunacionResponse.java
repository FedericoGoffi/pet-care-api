package com.saltoagro.pet_care_api.dto;

import java.time.LocalDate;

public class VacunacionResponse {

    private Long id;
    private String vacuna;
    private LocalDate fechaAplicacion;
    private LocalDate fechaVencimiento;
    private String veterinario;
    private String observaciones;

    public VacunacionResponse(
            Long id,
            String vacuna,
            LocalDate fechaAplicacion,
            LocalDate fechaVencimiento,
            String veterinario,
            String observaciones) {

        this.id = id;
        this.vacuna = vacuna;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaVencimiento = fechaVencimiento;
        this.veterinario = veterinario;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public String getVacuna() {
        return vacuna;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
