package com.saltoagro.pet_care_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(

        @NotBlank(message = "El username no puede estar vacío") @Size(min = 3, max = 15, message = "El username debe tener entre 3 y 15 caracteres") String username,

        @NotBlank(message = "La contraseña no puede estar vacía") @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres") String password

) {
}