package com.saltoagro.pet_care_api.exception;

public class MascotaNoEncontradaException extends RuntimeException {
    public MascotaNoEncontradaException(Long id) {
        super("Mascota no encontrada con id " + id);
    }
}
