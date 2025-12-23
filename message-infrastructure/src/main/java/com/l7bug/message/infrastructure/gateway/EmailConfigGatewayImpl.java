package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailConfigGateway;
import com.l7bug.message.infrastructure.dao.repository.EmailConfigRepository;
import com.l7bug.message.infrastructure.mapstruct.EmailConfigDoMapstruct;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Properties;

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
	public boolean testConnection(EmailConfig emailConfig) {
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		try {
			javaMailSender.testConnection();
			return true;
		} catch (MessagingException e) {
			log.error("邮件服务连接失败,原因", e);
			return false;
		}
	}

	@Override
	public Optional<EmailConfig> findById(Long id) {
		return emailConfigRepository.findById(id).map(emailConfigDoMapstruct::mapDomain);
	}
}
