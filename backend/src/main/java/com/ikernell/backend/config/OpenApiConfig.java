package com.ikernell.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ikernell-system API")
                        .version("1.0")
                        .description(
                                "Sistema de gestión de proyectos con control de presupuestos y automatización de workflow."))
                .addSecurityItem(new SecurityRequirement().addList("JavaBearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("JavaBearerAuth", new SecurityScheme()
                                .name("JavaBearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}