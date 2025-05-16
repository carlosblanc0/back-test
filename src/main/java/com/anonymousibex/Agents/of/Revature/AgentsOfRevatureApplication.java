package com.anonymousibex.Agents.of.Revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.anonymousibex.Agents.of.Revature")
public class AgentsOfRevatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentsOfRevatureApplication.class, args);
	}

}
