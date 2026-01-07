package com.l7bug.message.infrastructure.gateway;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ZipUtil;
import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailType;
import com.l7bug.message.domain.email.record.EmailRecord;
import com.l7bug.message.domain.email.record.EmailRecordGateway;
import com.l7bug.message.domain.email.record.Type;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class EmailConfigGatewayImplTest {
	private final Faker fakerCn = new Faker(Locale.CHINA);
	private final Faker faker = new Faker();
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
				emailRecord.setSendFiles(Map.of("test.txt", log, "testEmail.html", htmlByteArrayIS, "uuids.txt", sbIS));
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
		emailConfig.setPassword(faker.credentials().password());
		emailConfig.save();
	}

	@Test
	void getImapStore() {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		Assertions.assertThat(emailConfig.testConnection()).isPresent();
	}

	@Test
	@DisplayName("测试参数校验")
	void testValidate() throws Exception {
		Assertions.assertThatThrownBy(() -> this.emailConfigGateway.sendMessage(new EmailConfig(this.emailConfigGateway), new EmailRecord(this.emailRecordGateway), true)).message().isNotBlank().satisfies(log::info).as("测试邮件配置与邮件消息的参数都有问题").contains("邮箱类型不能为空", "邮箱名不能为空", "密码不能为空", "邮件主题不能为空", "邮件接收人不能为空", "邮件内容不能为空");
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		if (byId.isEmpty()) {
			return;
		}
		EmailRecord record = new EmailRecord(this.emailRecordGateway);
		Assertions.assertThatThrownBy(() -> this.emailConfigGateway.sendMessage(byId.get(), record, true)).message().isNotBlank().satisfies(log::info).contains("邮件主题不能为空", "邮件接收人不能为空", "邮件内容不能为空");
		Assertions.assertThatThrownBy(() -> this.emailConfigGateway.sendMessage(null, null, false)).message().isNotBlank().satisfies(log::info);
	}

	@Test
	@DisplayName("测试拉取邮件")
	void pullAllReadMessage() {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(2008381848064966657L);
		if (byId.isEmpty()) {
			return;
		}
		this.emailConfigGateway.pullAllReadMessage(byId.get(), record -> {
			record.setType(Type.RECEIVE);
			byte[] bytes = record.getContent().getBytes(StandardCharsets.UTF_8);
			byte[] gzip = ZipUtil.gzip(bytes);
			log.info("[压缩]{}::{}.减少了{}%体积", bytes.length, gzip.length, (bytes.length - gzip.length) * 1D / bytes.length);
			record.setContent(Base64.encode(gzip));
			emailRecordGateway.save(record);
		});
	}
}
