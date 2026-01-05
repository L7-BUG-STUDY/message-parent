package com.l7bug.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * App
 *
 * @author Administrator
 * @since 2026/1/5 11:24
 */
@SpringBootApplication
@EnableEurekaServer
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
