package com.l7bug.consul;

import com.l7bug.consul.feign.DemoClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author Administrator
 * @since 2026/1/14 11:22
 */
@AllArgsConstructor
@RestController
public class IndexController {
	private final DemoClient demoClient;

	@GetMapping
	public String hello() {
		return "hello world" + demoClient.emailProcess();
	}
}
