package com.unir.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//@SpringBootApplication(scanBasePackages = "com.unir.gateway")
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		// Retrieve execution profile from environment variable. Otherwise, default profile is used.
		/* String profile = System.getenv("PROFILE");
		System.setProperty("spring.profiles.active", profile != null ? profile : "default"); */
		SpringApplication.run(GatewayApplication.class, args);
	}

}
