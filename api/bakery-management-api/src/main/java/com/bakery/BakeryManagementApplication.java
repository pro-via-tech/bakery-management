package com.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class BakeryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BakeryManagementApplication.class, args);
	}

}
