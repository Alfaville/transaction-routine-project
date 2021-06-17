package io.pismo.transaction_routine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "io.pismo.transaction_routine")
public class ApplicationRun implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationRun.class, args);
	}

}