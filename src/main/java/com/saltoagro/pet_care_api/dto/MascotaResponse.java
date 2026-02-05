package com.saltoagro.pet_care_api.dto;

import java.time.LocalDate;
import java.util.List;

import com.saltoagro.pet_care_api.model.Especie;

public class MascotaResponse {

    private Long id;
    private String nombre;
    private Especie especie;
    private LocalDate fechaNacimiento;
    private String sexo;
    private Double pesoKg;
    private boolean esterilizado;
    private List<VacunacionResponse> vacunaciones;

    public MascotaResponse(
            Long id,
            String nombre,
            Especie especie,
            LocalDate fechaNacimiento,
            String sexo,
            Double pesoKg,
            boolean esterilizado,
            List<VacunacionResponse> vacunaciones) {

        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.pesoKg = pesoKg;
        this.esterilizado = esterilizado;
        this.vacunaciones = vacunaciones;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Especie getEspecie() {
        return especie;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public boolean isEsterilizado() {
        return esterilizado;
    }

    public List<VacunacionResponse> getVacunaciones() {
        return vacunaciones;
    }

}