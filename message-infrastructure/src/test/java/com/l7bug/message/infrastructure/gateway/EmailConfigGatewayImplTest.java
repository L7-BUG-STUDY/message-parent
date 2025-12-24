package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailConfigGatewayImplTest {
	@Autowired
	private EmailConfigGatewayImpl emailConfigGateway;

	@Test
	void testConnection() {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId)
			.isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		String b = emailConfig.testConnection();
		assertThat(b).isEmpty();
		emailConfig.setPassword("123321");
		assertThat(emailConfig.testConnection()).isNotEmpty();

		byId = this.emailConfigGateway.findById(UUID.randomUUID().getMostSignificantBits());
		assertThat(byId).isNotNull()
			.isEmpty();
	}

	@Test
	void testSendMessage() throws IOException {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId)
			.isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/testEmail.html");) {
			if (resourceAsStream == null) {
				return;
			}
			InputStream log = this.getClass().getResourceAsStream("/test.txt");
			if (log == null) {
				return;
			}
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resourceAsStream.readAllBytes());
			String content = new String(byteArrayInputStream.readAllBytes(), StandardCharsets.UTF_8);
			boolean b = emailConfig.sendMessage("测试", content, Map.of("test.txt", log, "testEmail.html", byteArrayInputStream), "ll789y@gmail.com", "1411205284@qq.com");
			assertThat(b).isTrue();
		}
	}

}
