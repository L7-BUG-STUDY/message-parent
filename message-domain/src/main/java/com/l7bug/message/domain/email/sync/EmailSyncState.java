package com.l7bug.message.domain.email.sync;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * EmailSyncState
 *
 * @author Administrator
 * @since 2026/1/8 17:58
 */
@Data
public class EmailSyncState {
	@Getter(AccessLevel.PRIVATE)
	private final EmailSyncStateGateway emailSyncStateGateway;
	private Long id;

	@NotBlank(message = "用户名不能为空")
	private String username;

	@NotBlank(message = "文件夹不能为空")
	private String folder;

	@NotNull(message = "UID验证不能为空")
	private Long uidValidity;

	@NotNull(message = "UID不能为空")
	private Long uid;

	/**
	 * 保存邮箱同步状态
	 *
	 * @return 保存操作是否成功，成功返回true，失败返回false
	 */
	public boolean save() {
		return emailSyncStateGateway.save(this);
	}
}
