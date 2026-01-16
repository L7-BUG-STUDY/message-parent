package com.l7bug.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * GatewayApp
 *
 * @author Administrator
 * @since 2026/1/15 18:15
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApp {
	static void main() {
		SpringApplication.run(GatewayApp.class);
	}
}
