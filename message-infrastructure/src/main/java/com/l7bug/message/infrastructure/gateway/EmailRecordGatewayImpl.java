package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.record.EmailRecord;
import com.l7bug.message.domain.email.record.EmailRecordGateway;
import com.l7bug.message.infrastructure.dao.dataobject.EmailRecordDo;
import com.l7bug.message.infrastructure.dao.repository.EmailRecordRepository;
import com.l7bug.message.infrastructure.mapstruct.EmailRecordDoMapstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * EmailRecordGatewayImpl
 *
 * @author Administrator
 * @since 2025/12/29 17:37
 */
@AllArgsConstructor
@Component
public class EmailRecordGatewayImpl implements EmailRecordGateway {
	private final EmailRecordDoMapstruct emailRecordDoMapstruct;
	private final EmailRecordRepository emailRecordRepository;

	@Override
	public boolean save(EmailRecord emailRecord) {
		EmailRecordDo emailRecordDo = emailRecordDoMapstruct.mapDo(emailRecord);
		emailRecordRepository.save(emailRecordDo);
		emailRecord.setId(emailRecordDo.getId());
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
