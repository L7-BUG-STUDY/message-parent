package com.l7bug.message.domain.email;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * EmailGateway
 *
 * @author Administrator
 * @since 2025/12/23 14:14
 */
public interface EmailConfigGateway {
	String testConnection(EmailConfig emailConfig);

	void sendMessage(EmailConfig emailConfig, String subject, String content, Map<String, InputStream> files, String... to) throws Exception;

	Optional<EmailConfig> findById(Long id);
}
