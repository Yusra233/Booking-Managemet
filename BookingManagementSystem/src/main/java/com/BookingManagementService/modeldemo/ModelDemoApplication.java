package com.BookingManagementService.modeldemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EntityScan("org.example.model")
@EnableFeignClients
@EnableDiscoveryClient
@OpenAPIDefinition(
		info = @Info(title = "Book Management Service",version ="1.0.1" ),
		servers = {@Server(url = "http://localhost:8080"),@Server(url = "http://BookManagement.com")},
		tags = {@Tag(name = "Book Operations")}

)
public class ModelDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModelDemoApplication.class, args);
	}
}

