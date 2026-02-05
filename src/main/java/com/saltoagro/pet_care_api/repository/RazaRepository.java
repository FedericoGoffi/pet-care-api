package com.saltoagro.pet_care_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saltoagro.pet_care_api.model.Raza;

public interface RazaRepository extends JpaRepository<Raza, Long> {
    Raza findByNombreIgnoreCase(String nombre);
}