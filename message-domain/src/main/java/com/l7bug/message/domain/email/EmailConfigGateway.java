package com.l7bug.message.domain.email;

import com.l7bug.message.domain.email.record.EmailRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * EmailGateway
 *
 * @author Administrator
 * @since 2025/12/23 14:14
 */
@Validated
public interface EmailConfigGateway {
	/**
	 * 保存邮件配置
	 *
	 * @param emailConfig 邮件配置对象
	 * @return 保存成功返回true，否则返回false
	 */
	boolean save(@NotNull(message = "邮件配置不能为空") @Valid EmailConfig emailConfig);

	/**
	 * 测试邮件配置连接
	 *
	 * @param emailConfig 邮件配置对象，包含SMTP服务器、用户名、密码等信息
	 * @return 连接成功时返回空字符串，连接失败时返回错误信息
	 */
	Optional<String> testConnection(@NotNull(message = "邮件配置不能为空") @Valid EmailConfig emailConfig);

	void sendMessage(@NotNull(message = "邮件配置不能为空") @Valid EmailConfig emailConfig, @NotNull(message = "邮件消息") @Validated(EmailValidGroups.Send.class) EmailRecord record, boolean canFileZip) throws Exception;

	Optional<EmailConfig> findById(Long id);
}
