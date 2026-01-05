package com.l7bug.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * App
 *
 * @author Administrator
 * @since 2026/1/5 11:57
 */
@SpringBootApplication
@EnableConfigServer
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
