package com.saltoagro.pet_care_api.dto;

public class VacunaResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private int mesesValidez;

    public VacunaResponse(
            Long id,
            String nombre,
            String descripcion,
            int mesesValidez) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.mesesValidez = mesesValidez;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getMesesValidez() {
        return mesesValidez;
    }
}