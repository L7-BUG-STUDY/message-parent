package com.l7bug.message.domain.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("EmailConfig 单元测试")
class EmailConfigTest {
	private EmailConfigGateway emailConfigGateway;
	private EmailConfig emailConfig;
	private EmailType emailType;

	@BeforeEach
	void setUp() {
		emailConfigGateway = Mockito.mock(EmailConfigGateway.class);
		emailType = EmailType.WX_WORK;
		emailConfig = new EmailConfig(emailConfigGateway);
		emailConfig.setType(emailType);
		emailConfig.setUsername("test@example.com");
		emailConfig.setPassword("password");
	}

	@Test
	@DisplayName("测试连接成功")
	void testConnectionSuccess() {
		// 模拟连接成功的情况
		when(emailConfigGateway.testConnection(any(EmailConfig.class))).thenReturn("");

		String result = emailConfig.testConnection();

		Assertions.assertThat(result).isEmpty();
		Assertions.assertThat(emailConfig.getConnection()).isTrue();
		verify(emailConfigGateway, times(1)).testConnection(any(EmailConfig.class));
		verify(emailConfigGateway, times(1)).save(any(EmailConfig.class));
	}

	@Test
	@DisplayName("测试连接失败")
	void testConnectionFailure() {
		// 模拟连接失败的情况
		String errorMessage = "Connection failed";
		when(emailConfigGateway.testConnection(any(EmailConfig.class))).thenReturn(errorMessage);

		String result = emailConfig.testConnection();

		Assertions.assertThat(result).isEqualTo(errorMessage);
		Assertions.assertThat(emailConfig.getConnection()).isFalse();
		verify(emailConfigGateway, times(1)).testConnection(any(EmailConfig.class));
		verify(emailConfigGateway, times(1)).save(any(EmailConfig.class));
	}

	@Test
	@DisplayName("测试保存功能")
	void testSave() {
		when(emailConfigGateway.save(any(EmailConfig.class))).thenReturn(true);

		boolean result = emailConfig.save();

		Assertions.assertThat(result).isTrue();
		verify(emailConfigGateway, times(1)).save(any(EmailConfig.class));
	}

	@Test
	@DisplayName("测试发送邮件成功")
	void testSendMessageSuccess() {
		Map<String, InputStream> files = new HashMap<>();

		try {
			Mockito.doNothing().when(emailConfigGateway).sendMessage(
				any(EmailConfig.class),
				any(String.class),
				any(String.class),
				any(Map.class),
				true, any(String.class)
			);

			boolean result = emailConfig.sendMessage("Subject", "Content", files, "recipient@example.com");

			Assertions.assertThat(result).isTrue();
			verify(emailConfigGateway, times(1)).sendMessage(
				any(EmailConfig.class),
				any(String.class),
				any(String.class),
				any(Map.class),
				true, any(String.class)
			);
		} catch (Exception e) {
			Assertions.fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("测试发送邮件失败")
	void testSendMessageFailure() {
		Map<String, InputStream> files = new HashMap<>();

		try {
			Mockito.doThrow(new RuntimeException("Send failed")).when(emailConfigGateway).sendMessage(
				any(EmailConfig.class),
				any(String.class),
				any(String.class),
				any(Map.class),
				true, any(String.class)
			);

			boolean result = emailConfig.sendMessage("Subject", "Content", files, "recipient@example.com");

			Assertions.assertThat(result).isFalse();
		} catch (Exception e) {
			Assertions.fail("Unexpected exception: " + e.getMessage());
		}
	}
}
