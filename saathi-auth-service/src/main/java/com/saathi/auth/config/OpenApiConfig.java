package com.saathi.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic OpenAPI configuration to provide metadata visible in Swagger UI.
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Testing API")
            .version("v1")
            .description("API documentation for the Example project")
            .contact(new Contact().name("Saathi").email("dev@localhost"))
            .license(new License().name("Apache 2.0").url("http://springdoc.org"))
        );
  }
}

