package com.l7bug.message.domain.email;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * EmailGateway
 *
 * @author Administrator
 * @since 2025/12/23 14:14
 */
public interface EmailConfigGateway {
	/**
	 * 保存邮件配置
	 *
	 * @param emailConfig 邮件配置对象
	 * @return 保存成功返回true，否则返回false
	 */
	boolean save(EmailConfig emailConfig);

	/**
	 * 测试邮件配置连接
	 *
	 * @param emailConfig 邮件配置对象，包含SMTP服务器、用户名、密码等信息
	 * @return 连接成功时返回空字符串，连接失败时返回错误信息
	 */
	String testConnection(EmailConfig emailConfig);

	/**
	 * 发送邮件
	 *
	 * @param emailConfig 邮件配置对象，包含SMTP服务器信息和认证凭据
	 * @param subject     邮件主题
	 * @param content     邮件内容（HTML格式）
	 * @param files       附件映射，键为文件名，值为文件输入流
	 * @param to          收件人邮箱地址列表
	 * @throws Exception 发送过程中可能出现的异常
	 */
	void sendMessage(EmailConfig emailConfig, String subject, String content, Map<String, InputStream> files, String... to) throws Exception;

	Optional<EmailConfig> findById(Long id);
}
