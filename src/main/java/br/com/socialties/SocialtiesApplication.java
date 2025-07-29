package br.com.socialties;

import br.com.socialties.domain.storage.StorageProperties;
import br.com.socialties.domain.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SocialtiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialtiesApplication.class, args);
	}

}
