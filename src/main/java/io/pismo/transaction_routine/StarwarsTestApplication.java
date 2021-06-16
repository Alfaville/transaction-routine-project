package io.pismo.transaction_routine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kuber.starwarstest")
public class StarwarsTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarwarsTestApplication.class, args);
	}

}
