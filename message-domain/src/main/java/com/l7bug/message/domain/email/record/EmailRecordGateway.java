package com.l7bug.message.domain.email.record;

import com.l7bug.message.domain.email.EmailConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

/**
 * EmailRecordGateway
 *
 * @author Administrator
 * @since 2025/12/29 11:41
 */
public interface EmailRecordGateway {
	boolean save(EmailRecord emailRecord);

	Optional<EmailRecord> findById(Long id);

	boolean sendByConfig(@Valid EmailRecord emailRecord, @NotNull(message = "邮件配置不能为空") EmailConfig emailConfig);
}
