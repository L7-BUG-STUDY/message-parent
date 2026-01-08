package com.l7bug.message.domain.email.sync;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * EmailSyncStateGateway
 *
 * @author Administrator
 * @since 2026/1/8 18:01
 */
@Validated
public interface EmailSyncStateGateway {

	boolean save(@NotNull(message = "邮件同步点位不能为空") @Valid EmailSyncState save);
}
