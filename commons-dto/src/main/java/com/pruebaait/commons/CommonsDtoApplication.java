package com.pruebaait.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CommonsDtoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonsDtoApplication.class, args);
	}

}
