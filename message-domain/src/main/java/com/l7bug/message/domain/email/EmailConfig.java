package com.l7bug.message.domain.email;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;

/**
 * EmailConfig
 *
 * @author Administrator
 * @since 2025/12/23 13:50
 */
@Slf4j
@Data
public class EmailConfig {
	@Getter(AccessLevel.PRIVATE)
	private final EmailConfigGateway emailConfigGateway;
	private Long id;
	private EmailType type;
	private String username;
	private String password;
	private Boolean connection = false;

	public String testConnection() {
		String s = emailConfigGateway.testConnection(this);
		this.setConnection(s.isEmpty());
		this.save();
		return s;
	}

	public boolean save() {
		return this.getEmailConfigGateway().save(this);
	}

	public boolean sendMessage(String subject, String content, Map<String, InputStream> files, String... to) {
		try {
			this.emailConfigGateway.sendMessage(this, subject, content, files, true, to);
			this.setConnection(true);
			return true;
		} catch (Exception e) {
			log.error("邮件发送失败!", e);
			return false;
		}
	}
}
