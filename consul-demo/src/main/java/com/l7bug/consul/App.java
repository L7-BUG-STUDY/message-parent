package com.l7bug.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App
 *
 * @author Administrator
 * @since 2026/1/13 16:05
 */
@RestController
@SpringBootApplication
public class App {
	static void main() {
		SpringApplication.run(App.class);
	}

	@GetMapping("/")
	public String hello() {
		return "hello world";
	}
}
