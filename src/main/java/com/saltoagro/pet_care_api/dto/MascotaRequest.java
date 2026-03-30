package com.saltoagro.pet_care_api.dto;

import java.time.LocalDate;

import com.saltoagro.pet_care_api.model.Especie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MascotaRequest(

        @NotBlank(message = "El nombre no puede estar vacío") String nombre,

        @NotNull(message = "La especie es obligatoria") Especie especie,

        LocalDate fechaNacimiento,

        String sexo,

        @Positive(message = "El peso debe ser un valor positivo") Double pesoKg,

        boolean esterilizado

) {
}