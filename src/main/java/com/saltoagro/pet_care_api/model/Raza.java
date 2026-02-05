package com.saltoagro.pet_care_api.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "razas")
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Enumerated(EnumType.STRING)
    private Tamaño tamaño;

    @Enumerated(EnumType.STRING)
    private NivelActividad nivelActividad;

    @ElementCollection
    @CollectionTable(name = "raza_caracteristicas", joinColumns = @JoinColumn(name = "raza_id"))
    @Column(name = "caracteristica")
    private Set<String> caracteristicas;

    public Raza() {
    }

    public Raza(String nombre, Especie especie, Tamaño tamaño, NivelActividad nivelActividad) {
        this.nombre = nombre;
        this.especie = especie;
        this.tamaño = tamaño;
        this.nivelActividad = nivelActividad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Tamaño getTamano() {
        return tamaño;
    }

    public void setTamano(Tamaño tamano) {
        this.tamaño = tamano;
    }

    public NivelActividad getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(NivelActividad nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public Set<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(Set<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}