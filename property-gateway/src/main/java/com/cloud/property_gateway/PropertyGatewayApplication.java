package com.cloud.property_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PropertyGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertyGatewayApplication.class, args);
	}


}
