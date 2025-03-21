package com.gustavo.autocontrato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AutoContratoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutoContratoApplication.class, args);
	}
}
