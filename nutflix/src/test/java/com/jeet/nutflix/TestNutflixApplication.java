package com.jeet.nutflix;

import org.springframework.boot.SpringApplication;

public class TestNutflixApplication {

	public static void main(String[] args) {
		SpringApplication.from(NutflixApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
