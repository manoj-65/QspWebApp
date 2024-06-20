package com.alpha.qspiderrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EntityScan(basePackages = "com.alpha.qspiderrestapi.converter")
//@EnableJpaRepositories(converters = BranchCourseDtoConverter.class)
public class QspiderRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QspiderRestApiApplication.class, args);

	}
}
