package com.l7bug.message.domain.email;

import com.l7bug.message.domain.email.record.EmailRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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
		when(emailConfigGateway.testConnection(any(EmailConfig.class))).thenReturn(Optional.empty());

		var result = emailConfig.testConnection();

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
		when(emailConfigGateway.testConnection(any(EmailConfig.class))).thenReturn(Optional.of(errorMessage));

		var result = emailConfig.testConnection();

		// 分别验证结果和连接状态
		Assertions.assertThat(result).isNotEmpty();
		Assertions.assertThat(result.get()).isEqualTo(errorMessage);
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
	void testSendMessageSuccess() throws Exception {
		EmailRecord emailRecord = new EmailRecord(Mockito.mock());
		Mockito.doNothing().when(emailConfigGateway).sendMessage(
			emailConfig,
			emailRecord,
			true
		);

		boolean result = emailConfig.send(emailRecord, true);

		Assertions.assertThat(result).isTrue();
		verify(emailConfigGateway, times(1)).sendMessage(
			emailConfig, emailRecord, true
		);
	}

	@Test
	@DisplayName("测试发送邮件失败")
	void testSendMessageFailure() throws Exception {
		EmailRecord emailRecord = new EmailRecord(Mockito.mock());
		Mockito.doThrow(new RuntimeException("Send failed")).when(emailConfigGateway).sendMessage(
			emailConfig, emailRecord, false
		);

		boolean result = emailConfig.send(emailRecord, false);

		Assertions.assertThat(result).isFalse();
		emailConfig.send(emailRecord);
	}
}
