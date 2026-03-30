package com.saltoagro.pet_care_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.saltoagro.pet_care_api.model.Rol;
import com.saltoagro.pet_care_api.model.Usuario;
import com.saltoagro.pet_care_api.repository.RolRepository;
import com.saltoagro.pet_care_api.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.vet.password}")
    private String vetPassword;

    @Value("${app.user.password}")
    private String userPassword;

    @Bean
    CommandLineRunner initData(
            RolRepository rolRepo,
            UsuarioRepository userRepo,
            PasswordEncoder encoder) {

        return args -> {

            Rol admin = rolRepo.findByNombre("ROLE_ADMIN")
                    .orElseGet(() -> rolRepo.save(new Rol("ROLE_ADMIN")));

            Rol user = rolRepo.findByNombre("ROLE_USER")
                    .orElseGet(() -> rolRepo.save(new Rol("ROLE_USER")));

            Rol vet = rolRepo.findByNombre("ROLE_VETERINARIO")
                    .orElseGet(() -> rolRepo.save(new Rol("ROLE_VETERINARIO")));

            if (userRepo.count() == 0) {

                Usuario adminUser = new Usuario();
                adminUser.setUsername("admin");
                adminUser.setPassword(encoder.encode(adminPassword));
                adminUser.getRoles().add(admin);

                Usuario vetUser = new Usuario();
                vetUser.setUsername("vet");
                vetUser.setPassword(encoder.encode(vetPassword));
                vetUser.getRoles().add(vet);

                Usuario normalUser = new Usuario();
                normalUser.setUsername("user");
                normalUser.setPassword(encoder.encode(userPassword));
                normalUser.getRoles().add(user);

                userRepo.save(adminUser);
                userRepo.save(vetUser);
                userRepo.save(normalUser);
            }
        };
    }
}