package com.likelion.allForOne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AllForOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllForOneApplication.class, args);
	}

}
