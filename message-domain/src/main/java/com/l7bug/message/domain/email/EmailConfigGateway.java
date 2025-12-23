package com.l7bug.message.domain.email;

import java.util.Optional;

/**
 * EmailGateway
 *
 * @author Administrator
 * @since 2025/12/23 14:14
 */
public interface EmailConfigGateway {
	boolean testConnection(EmailConfig emailConfig);

	Optional<EmailConfig> findById(Long id);
}
