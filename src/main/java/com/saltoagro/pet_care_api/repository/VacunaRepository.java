package com.saltoagro.pet_care_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saltoagro.pet_care_api.model.Vacuna;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {

    Optional<Vacuna> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}