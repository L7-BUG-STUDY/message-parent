package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailConfigGateway;
import com.l7bug.message.infrastructure.dao.repository.EmailConfigRepository;
import com.l7bug.message.infrastructure.mapstruct.EmailConfigDoMapstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * MailConfigGateway
 *
 * @author Administrator
 * @since 2025/12/23 15:36
 */
@Slf4j
@Component
@AllArgsConstructor
public class EmailConfigGatewayImpl implements EmailConfigGateway {
	private final EmailConfigRepository emailConfigRepository;

	private final EmailConfigDoMapstruct emailConfigDoMapstruct;

	public JavaMailSenderImpl buildSender(EmailConfig emailConfig) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(emailConfig.getType().getSmtpHost());
		sender.setPort(emailConfig.getType().getSmtpPort());
		sender.setUsername(emailConfig.getUsername());
		sender.setPassword(emailConfig.getPassword());
		sender.setDefaultEncoding("UTF-8");
		Properties props = sender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		// 如果端口是 465，通常需要开启 SSL
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		return sender;
	}

	@Override
	public String testConnection(EmailConfig emailConfig) {
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		try {
			javaMailSender.testConnection();
			return "";
		} catch (MessagingException e) {
			log.error("邮件服务连接失败,原因", e);
			return e.getMessage();
		}
	}

	@Override
	public Optional<EmailConfig> findById(Long id) {
		return emailConfigRepository.findById(id).map(emailConfigDoMapstruct::mapDomain);
	}

	@Override
	public void sendMessage(EmailConfig emailConfig, String subject, String content, Map<String, InputStream> files, String... to) throws Exception {
		String testConnection = emailConfig.testConnection();
		if (!testConnection.isEmpty()) {
			throw new RuntimeException("[邮件连接失败]::" + testConnection);
		}
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		MimeMessage message = javaMailSender.createMimeMessage();
		// 2. 使用 MimeMessageHelper，第二个参数 true 表示需要支持 multipart（附件、HTML）
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setFrom(emailConfig.getUsername());
		helper.setTo(to);
		helper.setSubject(subject);

		// 3. 设置 HTML 内容
		// 第二个参数 true 表示发送的是 HTML，如果是 false 则会被当做纯文本显示 HTML 源码
		helper.setText(content, true);

		if (!files.isEmpty()) {
			// 4. 添加附件
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8)) {
				for (Map.Entry<String, InputStream> entry : files.entrySet()) {
					String fileName = entry.getKey();
					InputStream is = entry.getValue();
					try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(is.readAllBytes())) {
						ZipEntry zipEntry = new ZipEntry(fileName);
						zipOutputStream.putNextEntry(zipEntry);
						byteArrayInputStream.transferTo(zipOutputStream);
						zipOutputStream.closeEntry();
					}
				}
				zipOutputStream.finish();
				String encodedFileName = MimeUtility.encodeText("attachments.zip");
				helper.addAttachment(encodedFileName, new ByteArrayResource(byteArrayOutputStream.toByteArray()));
			}
		}

		// 5. 发送
		javaMailSender.send(message);
	}
}
