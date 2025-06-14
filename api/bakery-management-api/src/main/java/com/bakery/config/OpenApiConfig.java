package com.bakery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Value("${spring.application.name}")
	private String appName;

	@Value("${app.version:1.0.0}")
	private String appVersion;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title(appName.toUpperCase() + " API")
				.version("1.0.0")
				.description("This is a bakery management application."));
	}
}
