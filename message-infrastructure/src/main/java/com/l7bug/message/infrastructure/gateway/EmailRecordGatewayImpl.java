package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.record.EmailRecord;
import com.l7bug.message.domain.email.record.EmailRecordGateway;

import java.util.Optional;

/**
 * EmailRecordGatewayImpl
 *
 * @author Administrator
 * @since 2025/12/29 17:37
 */
public class EmailRecordGatewayImpl implements EmailRecordGateway {
	@Override
	public boolean save(EmailRecord emailRecord) {
		return true;
	}

	@Override
	public Optional<EmailRecord> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public boolean sendByConfig(EmailRecord emailRecord, EmailConfig emailConfig) {
		return false;
	}
}
