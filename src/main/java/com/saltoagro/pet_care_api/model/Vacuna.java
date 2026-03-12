package com.saltoagro.pet_care_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vacunas")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private int mesesValidez;

    public Vacuna() {
    }

    public Vacuna(String nombre, String descripcion, int mesesValidez) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.mesesValidez = mesesValidez;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getMesesValidez() {
        return mesesValidez;
    }
}