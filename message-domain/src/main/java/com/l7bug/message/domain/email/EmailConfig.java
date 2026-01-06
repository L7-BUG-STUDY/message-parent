package com.l7bug.message.domain.email;

import com.l7bug.message.domain.email.record.EmailRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

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
	@NotNull(message = "邮箱类型不能为空")
	private EmailType type;
	@NotNull(message = "邮箱名不能为空")
	private String username;
	@NotNull(message = "密码不能为空")
	private String password;
	private Boolean connection = false;

	public Optional<String> testConnection() {
		var s = emailConfigGateway.testConnection(this);
		this.setConnection(s.isEmpty());
		this.save();
		return s;
	}

	public boolean save() {
		return this.getEmailConfigGateway().save(this);
	}

	public boolean sendMessage(String subject, String content, Map<String, InputStream> files, boolean canFilesZip, String... to) {
		try {
			this.emailConfigGateway.sendMessage(this, subject, content, files, canFilesZip, to);
			this.setConnection(true);
			return true;
		} catch (Exception e) {
			log.error("邮件发送失败!", e);
			return false;
		}
	}

	public boolean send(EmailRecord emailRecord) {
		return true;
	}
}
