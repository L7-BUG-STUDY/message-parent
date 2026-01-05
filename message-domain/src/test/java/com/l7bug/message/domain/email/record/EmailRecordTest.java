package com.l7bug.message.domain.email.record;

import com.l7bug.message.domain.email.EmailConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailRecordTest {

	@Mock
	private EmailRecordGateway emailRecordGateway;

	@Mock
	private EmailConfig emailConfig;

	private EmailRecord emailRecord;

	@BeforeEach
	void setUp() {
		// 直接通过构造函数创建EmailRecord实例
		emailRecord = new EmailRecord(emailRecordGateway);
	}


	@Test
	void testConstructorInitialization() {
		assertThat(emailRecord).isNotNull();
	}

	@Test
	void testSetAndGetId() {
		Long expectedId = 1L;
		emailRecord.setId(expectedId);
		assertThat(emailRecord.getId()).isEqualTo(expectedId);
	}

	@Test
	void testSetAndGetEmailRecordType() {
		Type expectedType = Type.SEND;
		emailRecord.setType(expectedType);
		assertThat(emailRecord.getType()).isEqualTo(expectedType);
	}

	@Test
	void testSetAndGetFolder() {
		String expectedFolder = "inbox";
		emailRecord.setFolder(expectedFolder);
		assertThat(emailRecord.getFolder()).isEqualTo(expectedFolder);
	}

	@Test
	void testSetAndGetSubject() {
		String expectedSubject = "Test Subject";
		emailRecord.setSubject(expectedSubject);
		assertThat(emailRecord.getSubject()).isEqualTo(expectedSubject);
	}

	@Test
	void testSetAndGetFromAddress() {
		List<String> expectedFromAddress = List.of("sender@example.com");
		emailRecord.setFromAddress(expectedFromAddress);
		assertThat(emailRecord.getFromAddress()).isEqualTo(expectedFromAddress);
	}

	@Test
	void testSetAndGetRecipients() {
		List<String> expectedRecipients = List.of("recipient@example.com");
		emailRecord.setRecipients(expectedRecipients);
		assertThat(emailRecord.getRecipients()).isEqualTo(expectedRecipients);
	}

	@Test
	void testSetAndGetSentDate() {
		LocalDateTime expectedSentDate = LocalDateTime.now();
		emailRecord.setSentDate(expectedSentDate);
		assertThat(emailRecord.getSentDate()).isEqualTo(expectedSentDate);
	}

	@Test
	void testSetAndGetReceivedDate() {
		LocalDateTime expectedReceivedDate = LocalDateTime.now();
		emailRecord.setReceivedDate(expectedReceivedDate);
		assertThat(emailRecord.getReceivedDate()).isEqualTo(expectedReceivedDate);
	}

	@Test
	void testSetAndgetContent() {
		String expectedContent = "Test content";
		emailRecord.setContent(expectedContent);
		assertThat(emailRecord.getContent()).isEqualTo(expectedContent);
	}

	@Test
	void testSetAndGetFiles() {
		Map<String, String> expectedFiles = Map.of("file1", "filename1");
		emailRecord.setFiles(expectedFiles);
		assertThat(emailRecord.getFiles()).isEqualTo(expectedFiles);
	}

	@Test
	void testSendWithEmptyRecipients() {
		emailRecord.setRecipients(List.of());

		boolean result = emailRecord.send(emailConfig);

		assertThat(result).isFalse();
		verify(emailRecordGateway, never()).sendByConfig(any(), any());
		verify(emailRecordGateway, never()).save(any());
	}

	@Test
	void testSendSuccessfully() {
		// 准备测试数据
		emailRecord.setRecipients(List.of("recipient@example.com"));
		emailRecord.setSubject("Test Subject");
		emailRecord.setContent("Test content");
		when(emailConfig.getUsername()).thenReturn("sender@example.com");
		when(emailRecordGateway.sendByConfig(any(EmailRecord.class), any(EmailConfig.class))).thenReturn(true);
		when(emailRecordGateway.save(any(EmailRecord.class))).thenReturn(true);

		boolean result = emailRecord.send(emailConfig);

		assertThat(result).isTrue();
		verify(emailRecordGateway, times(1)).sendByConfig(any(EmailRecord.class), any(EmailConfig.class));
		verify(emailRecordGateway, times(1)).save(any(EmailRecord.class));
		assertThat(emailRecord.getReceivedDate()).isNotNull();
		assertThat(emailRecord.getSentDate()).isNotNull();
		assertThat(emailRecord.getFromAddress()).isEqualTo(List.of("sender@example.com"));
	}

	@Test
	void testSendFailedToSend() {
		// 准备测试数据
		emailRecord.setRecipients(List.of("recipient@example.com"));
		emailRecord.setSubject("Test Subject");
		emailRecord.setContent("Test content");
		when(emailConfig.getUsername()).thenReturn("sender@example.com");
		when(emailRecordGateway.sendByConfig(any(EmailRecord.class), any(EmailConfig.class))).thenReturn(false);

		boolean result = emailRecord.send(emailConfig);

		assertThat(result).isFalse();
		verify(emailRecordGateway, times(1)).sendByConfig(any(EmailRecord.class), any(EmailConfig.class));
		verify(emailRecordGateway, never()).save(any(EmailRecord.class));
	}

	@Test
	void testSendFailedToSave() {
		// 准备测试数据
		emailRecord.setRecipients(List.of("recipient@example.com"));
		emailRecord.setSubject("Test Subject");
		emailRecord.setContent("Test content");
		when(emailConfig.getUsername()).thenReturn("sender@example.com");
		when(emailRecordGateway.sendByConfig(any(EmailRecord.class), any(EmailConfig.class))).thenReturn(true);
		when(emailRecordGateway.save(any(EmailRecord.class))).thenReturn(false);

		boolean result = emailRecord.send(emailConfig);

		assertThat(result).isFalse();
		verify(emailRecordGateway, times(1)).sendByConfig(any(EmailRecord.class), any(EmailConfig.class));
		verify(emailRecordGateway, times(1)).save(any(EmailRecord.class));
	}
}
