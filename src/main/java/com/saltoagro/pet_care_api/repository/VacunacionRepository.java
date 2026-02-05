package com.saltoagro.pet_care_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saltoagro.pet_care_api.model.Vacunacion;

@Repository
public interface VacunacionRepository extends JpaRepository<Vacunacion, Long> {

        boolean existsByMascotaIdAndVacunaIdAndFechaVencimientoAfter(
                        Long mascotaId,
                        Long vacunaId,
                        LocalDate fecha);

        List<Vacunacion> findByMascotaIdAndFechaVencimientoBefore(
                        Long mascotaId,
                        LocalDate fecha);

        List<Vacunacion> findByMascotaIdAndFechaVencimientoBetween(
                        Long mascotaId,
                        LocalDate desde,
                        LocalDate hasta);
}
