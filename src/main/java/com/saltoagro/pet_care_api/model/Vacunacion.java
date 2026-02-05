package com.saltoagro.pet_care_api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "vacunaciones")
public class Vacunacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mascota_id")
    @JsonIgnore
    private Mascota mascota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vacuna_id")
    private Vacuna vacuna;

    @Column(nullable = false)
    private LocalDate fechaAplicacion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    private String veterinario;
    private String observaciones;

    protected Vacunacion() {
    }

    public Vacunacion(
            Mascota mascota,
            Vacuna vacuna,
            LocalDate fechaAplicacion,
            String veterinario,
            String observaciones) {
        this.mascota = mascota;
        this.vacuna = vacuna;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaVencimiento = fechaAplicacion.plusMonths(vacuna.getMesesValidez());
        this.veterinario = veterinario;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}