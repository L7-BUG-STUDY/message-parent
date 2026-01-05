package com.l7bug.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author Administrator
 * @since 2026/1/5 14:17
 */
@RefreshScope
@RestController
public class IndexController {

	@Value("${user.id}")
	private String userId;
	
	@GetMapping
	public String hello() {
		return userId;
	}
}
