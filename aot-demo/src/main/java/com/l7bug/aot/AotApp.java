package com.l7bug.aot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * AotApp
 *
 * @author Administrator
 * @since 2026/1/5 17:02
 */
@RestController
@SpringBootApplication
public class AotApp {
	public static void main(String[] args) {
		SpringApplication.run(AotApp.class, args);
	}

	@GetMapping
	public String hello() {
		return UUID.randomUUID().toString();
	}
}
