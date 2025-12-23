package com.l7bug.message.domain.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailConfigTest {
	private EmailConfigGateway emailConfigGateway;

	@BeforeEach
	void setUp() {
		emailConfigGateway = Mockito.mock(EmailConfigGateway.class);
	}

	@Test
	void valid() {
		EmailConfig emailConfig = new EmailConfig(emailConfigGateway);
		Assertions.assertThat(emailConfig.valid())
			.isFalse()
		;
	}
}
