package com.l7bug.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * openfeign
 *
 * @author Administrator
 * @since 2026/1/14 11:18
 */
@EnableDiscoveryClient // 开启 Consul 服务发现
@SpringBootApplication
public class ConsulOpenfeignApp {
	public static void main(String[] args) {
		SpringApplication.run(ConsulOpenfeignApp.class, args);
	}
}
