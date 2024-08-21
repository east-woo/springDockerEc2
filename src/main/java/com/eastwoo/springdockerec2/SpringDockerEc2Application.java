package com.eastwoo.springdockerec2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringDockerEc2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringDockerEc2Application.class, args);
	}

}
