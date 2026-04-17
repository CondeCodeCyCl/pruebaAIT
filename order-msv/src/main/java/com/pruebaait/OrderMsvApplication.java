package com.pruebaait;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.pruebaait.commons.clients")
public class OrderMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMsvApplication.class, args);
	}

}
