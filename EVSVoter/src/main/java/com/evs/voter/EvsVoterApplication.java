package com.evs.voter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class EvsVoterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvsVoterApplication.class, args);
	}

}
