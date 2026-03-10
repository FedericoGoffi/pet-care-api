package com.saltoagro.pet_care_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI petCareOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Pet Care API")
                        .description("API para gestión de mascotas, vacunas y vacunaciones")
                        .version("1.0.0"));
    }
}