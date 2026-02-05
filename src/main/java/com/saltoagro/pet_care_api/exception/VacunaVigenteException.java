package com.saltoagro.pet_care_api.exception;

public class VacunaVigenteException extends RuntimeException {
    public VacunaVigenteException(String nombreVacuna) {
        super("La mascota ya tiene la vacuna " + nombreVacuna + " vigente");
    }
}
