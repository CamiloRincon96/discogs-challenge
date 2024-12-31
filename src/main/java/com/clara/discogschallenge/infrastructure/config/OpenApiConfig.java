package com.clara.discogschallenge.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI discographyApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Discography Comparison API")
                        .description("API for searching artists on Discogs, storing their discographies," +
                                " and comparing them")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Clara")
                                .url("https://github.com/CamiloRincon96/discogs-challenge")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development")
                ));
    }
} 