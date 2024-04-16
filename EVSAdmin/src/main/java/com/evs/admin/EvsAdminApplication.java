package com.evs.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@OpenAPIDefinition(
		info = @Info(title = "Election Voting System Admin Operations",
		description = "Admin can add/update/view/delete elections",
		version = "1.0.0",
		contact = @Contact(name = "Yaswanth Marni, Aditya taitkar",
						email = "yaswanthmarni7077@gmail.com, adityataitkar@gmail.com")))
@SpringBootApplication
@EnableDiscoveryClient
public class EvsAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvsAdminApplication.class, args);
	}

}
