package com.alpha.qspiderrestapi.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public OpenAPI usersMicroserviceOpenAPI() {

		Server production = new Server();
		production.setUrl("http://37.61.213.213:8080");
		production.setDescription("Production environment");
		
		Server dev = new Server();
		dev.setUrl("http://106.51.76.167:8080");
		dev.setDescription("Developement environment");

		Contact contact = new Contact();
		//what should the email be
		contact.setEmail("info@email.in");
		contact.setName("qspiders");
		
		//what should we set the url as ??
		contact.setUrl("url");

		//is there any changes to be made
		License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

		Info info = new Info().title("qspiders RESTful Web Service documentation").version("1.0").contact(contact)
				.description("This API exposes endpoints related to qspiders application.")
				//should we set this terms of service url
				.termsOfService("termsOfServiceUrl").license(mitLicense);

		return new OpenAPI().info(info).servers(List.of(production,dev));
	}

	
}
