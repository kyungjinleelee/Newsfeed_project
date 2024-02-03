package com.sparta.newsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing		// jpa auditing
@SpringBootApplication
public class NewsfeedApplication {

	public static void main(String[] args) {

		SpringApplication.run(NewsfeedApplication.class, args);
	}

}
