package com.l7bug.message.infrastructure.gateway;

import com.github.javafaker.Faker;
import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailType;
import com.l7bug.message.domain.email.record.EmailRecord;
import com.l7bug.message.domain.email.record.EmailRecordGateway;
import jakarta.mail.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailConfigGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private EmailConfigGatewayImpl emailConfigGateway;

	@Autowired
	private EmailRecordGateway emailRecordGateway;

	@Test
	void testConnection() {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		String password = emailConfig.getPassword();
		emailConfig.setPassword("123321");
		assertThat(emailConfig.testConnection()).isNotEmpty();
		assertThat(emailConfig.getConnection()).isFalse();
		emailConfig.setPassword(password);
		assertThat(emailConfig.testConnection()).isEmpty();
		assertThat(emailConfig.getConnection()).isTrue();
		byId = this.emailConfigGateway.findById(UUID.randomUUID().getMostSignificantBits());
		assertThat(byId).isNotNull().isEmpty();
	}

	@Test
	void testSendMessage() throws IOException {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/testEmail.html"); InputStream log = this.getClass().getResourceAsStream("/test.txt");) {
			if (resourceAsStream == null) {
				return;
			}
			if (log == null) {
				return;
			}
			byte[] htmlBytes = resourceAsStream.readAllBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 10000; i++) {
				sb.append(UUID.randomUUID());
			}
			try (ByteArrayInputStream htmlByteArrayIS = new ByteArrayInputStream(htmlBytes); ByteArrayInputStream sbIS = new ByteArrayInputStream(sb.toString().getBytes())) {
				String content = new String(htmlBytes, StandardCharsets.UTF_8);
				EmailRecord emailRecord = new EmailRecord(emailRecordGateway);
				emailRecord.setSubject("测试");
				emailRecord.setContent(content);
				emailRecord.setFiles(Map.of("test.txt", log, "testEmail.html", htmlByteArrayIS, "uuids.txt", sbIS));
				emailRecord.setRecipients(List.of("ll789y@gmail.com", "l7-bug@qq.com"));
				boolean b = emailConfig.send(emailRecord, true);
				assertThat(b).isTrue();
			}
		}
	}

	@Test
	void testSave() {
		EmailConfig emailConfig = new EmailConfig(emailConfigGateway);
		emailConfig.setType(EmailType.WX_WORK);
		emailConfig.setUsername(faker.internet().safeEmailAddress());
		emailConfig.setPassword(faker.internet().password());
		emailConfig.save();
	}

	@Test
	void getImapStore() throws Exception {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		Optional<Store> imapStore = this.emailConfigGateway.getImapStore(emailConfig);
		if (imapStore.isEmpty()) {
			return;
		}
		System.err.println(imapStore.get().getDefaultFolder());
		imapStore.get().close();
		System.err.println();
	}
}
