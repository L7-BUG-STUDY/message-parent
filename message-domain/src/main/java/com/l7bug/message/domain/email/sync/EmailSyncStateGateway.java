package com.l7bug.message.domain.email.sync;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * EmailSyncStateGateway
 *
 * @author Administrator
 * @since 2026/1/8 18:01
 */
@Validated
public interface EmailSyncStateGateway {

	/**
	 * 保存邮箱同步状态到数据库
	 *
	 * @param save 邮箱同步状态对象
	 * @return 保存操作是否成功，成功返回true，失败返回false
	 */
	boolean save(@NotNull(message = "邮件同步点位不能为空") @Valid EmailSyncState save);

	Optional<EmailSyncState> findLast(
		@NotBlank(message = "用户名不能为空") String username,
		@NotBlank(message = "文件夹不能为空") String folder,
		@NotNull(message = "UID验证不能为空") Long uidValidity
	);
}
