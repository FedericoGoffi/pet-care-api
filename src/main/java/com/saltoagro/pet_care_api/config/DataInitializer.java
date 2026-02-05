package com.saltoagro.pet_care_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saltoagro.pet_care_api.model.Vacuna;
import com.saltoagro.pet_care_api.repository.VacunaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarVacunas(VacunaRepository vacunaRepository) {
        return args -> {

            cargarSiNoExiste(vacunaRepository,
                    "Antirrábica", "Previene la rabia", 12);

            cargarSiNoExiste(vacunaRepository,
                    "Séxtuple", "Vacuna múltiple canina", 12);

            cargarSiNoExiste(vacunaRepository,
                    "Parvovirus", "Previene parvovirus", 12);

            cargarSiNoExiste(vacunaRepository,
                    "Moquillo", "Previene moquillo canino", 12);
        };
    }

    private void cargarSiNoExiste(
            VacunaRepository repository,
            String nombre,
            String descripcion,
            int mesesValidez) {

        if (!repository.existsByNombreIgnoreCase(nombre)) {
            repository.save(
                    new Vacuna(nombre, descripcion, mesesValidez));
        }
    }
}