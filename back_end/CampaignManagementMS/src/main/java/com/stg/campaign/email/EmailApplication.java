package com.stg.campaign.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:configuration.properties" })
public class EmailApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmailApplication.class, args);
	}

}
