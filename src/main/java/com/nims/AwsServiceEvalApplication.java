package com.nims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AwsServiceEvalApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsServiceEvalApplication.class, args);
	}

}
