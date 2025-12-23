package com.l7bug.message.domain.email;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * EmailConfig
 *
 * @author Administrator
 * @since 2025/12/23 13:50
 */
@Data
public class EmailConfig {
	@Getter(AccessLevel.PRIVATE)
	private final EmailConfigGateway emailConfigGateway;
	private Long id;
	private EmailType type;
	private String username;
	private String password;
	private Boolean connection = false;

	public boolean testConnection() {
		return emailConfigGateway.testConnection(this);
	}
}
