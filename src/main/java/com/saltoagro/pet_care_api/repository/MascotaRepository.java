package com.saltoagro.pet_care_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saltoagro.pet_care_api.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByUsuarioUsername(String username);

    Page<Mascota> findAll(Pageable pageable);
}