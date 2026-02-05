package com.saltoagro.pet_care_api.exception;

public class VacunaNoEncontradaException extends RuntimeException {
    public VacunaNoEncontradaException(Long id) {
        super("Vacuna no encontrada con id " + id);
    }
}
