package com.veiculosmg.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // ajuste o padrão conforme necessário
                .allowedOrigins("http://localhost:4200") // ajuste a URL do seu aplicativo Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE") // ajuste os métodos HTTP permitidos
                .allowedHeaders("*") // ajuste os cabeçalhos permitidos
                .allowCredentials(true); // permitir envio de cookies e autenticação
    }
}


