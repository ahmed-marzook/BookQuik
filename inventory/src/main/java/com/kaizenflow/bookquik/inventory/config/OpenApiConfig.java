package com.kaizenflow.bookquik.inventory.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.dev-url:http://localhost:8081}")
    private String devUrl;

    @Value("${openapi.prod-url:https://api.example.com}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("contact@example.com");
        contact.setName("API Support");
        contact.setUrl("https://www.example.com/support");

        License mitLicense =
                new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info =
                new Info()
                        .title("Inventory Service API")
                        .version("1.0")
                        .contact(contact)
                        .description("This API exposes endpoints to inventory service.")
                        .termsOfService("https://www.example.com/terms")
                        .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
