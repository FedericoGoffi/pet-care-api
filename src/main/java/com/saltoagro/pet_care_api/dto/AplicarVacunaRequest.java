package com.saltoagro.pet_care_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class AplicarVacunaRequest {

    @NotNull(message = "El id de la vacuna es obligatorio")
    private Long vacunaId;

    @NotNull(message = "La fecha de aplicación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaAplicacion;

    @NotBlank(message = "El nombre del veterinario es obligatorio")
    private String veterinario;

    private String observaciones;

    public AplicarVacunaRequest() {
    }

    public Long getVacunaId() {
        return vacunaId;
    }

    public void setVacunaId(Long vacunaId) {
        this.vacunaId = vacunaId;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}