package com.l7bug.message.domain.email;

import com.l7bug.message.domain.email.record.EmailRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.function.Consumer;

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

	@Validated(EmailValidGroups.Send.class)
	void sendMessage(@NotNull(message = "邮件配置不能为空") @Valid EmailConfig emailConfig,
					 @NotNull(message = "邮件消息不能为空") @Valid EmailRecord record,
					 boolean canFileZip) throws Exception;

	Optional<EmailConfig> findById(Long id);

	/**
	 * 拉取所有收件邮件
	 * 该方法连接到指定的邮件配置，检索所有邮件文件夹中的已读邮件，并通过消费者函数处理每封邮件
	 * 使用虚拟线程池并发处理邮件，提高处理效率
	 *
	 * @param emailConfig 邮件配置对象，包含连接邮件服务器所需的信息，如用户名、密码、服务器地址等
	 * @param consumer    消费者函数，用于处理拉取到的每一封邮件记录，接收EmailRecord类型的参数
	 */
	void pullAllReadMessage(@NotNull(message = "邮件配置不能为空") @Valid EmailConfig emailConfig, @NotNull Consumer<EmailRecord> consumer);
}
