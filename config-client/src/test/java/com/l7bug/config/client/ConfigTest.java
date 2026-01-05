package com.l7bug.config.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ConfigTest
 *
 * @author Administrator
 * @since 2026/1/5 12:17
 */
@SpringBootTest
public class ConfigTest {
	@Value("${user.id}")
	private String userId;

	@Test
	void test() {
		System.err.println(userId);
	}
}
