package com.saltoagro.pet_care_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saltoagro.pet_care_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

}
