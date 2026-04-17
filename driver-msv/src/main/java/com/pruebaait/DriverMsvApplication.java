package com.pruebaait;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.pruebaait.commons.clients") 
public class DriverMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverMsvApplication.class, args);
	}

}
