package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfigGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailConfigGatewayImplTest {
	@Autowired
	private EmailConfigGateway emailConfigGateway;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testConnection() {
	}
}
