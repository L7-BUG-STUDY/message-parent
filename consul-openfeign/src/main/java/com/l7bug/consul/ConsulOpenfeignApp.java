package com.l7bug.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * openfeign
 *
 * @author Administrator
 * @since 2026/1/14 11:18
 */
@EnableDiscoveryClient // 开启 Consul 服务发现
@EnableFeignClients(basePackages = "com.l7bug.consul.feign")
@SpringBootApplication
public class ConsulOpenfeignApp {
	static void main() {
		SpringApplication.run(ConsulOpenfeignApp.class);
	}
}
