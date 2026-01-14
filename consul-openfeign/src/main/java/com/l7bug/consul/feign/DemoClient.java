package com.l7bug.consul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * EmailClient
 *
 * @author Administrator
 * @since 2026/1/14 11:20
 */
@FeignClient(name = "consul-demo")
public interface DemoClient {
	@GetMapping("/demo")
	String emailProcess();
}
