package com.l7bug.message.domain.email.record;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailValidGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件记录
 *
 * @author Administrator
 * @since 2025/12/29 11:28
 */
@Data
public class EmailRecord {
	@Getter(AccessLevel.PRIVATE)
	private final EmailRecordGateway emailRecordGateway;
	private Long id;
	/**
	 * 邮件记录类型
	 */
	private Type type;
	/**
	 * 文件夹
	 */
	private String folder;
	/**
	 * 主题
	 */
	@NotBlank(message = "邮件主题不能为空", groups = EmailValidGroups.Send.class)
	private String subject;
	/**
	 * 发送人
	 */
	private List<String> fromAddress;
	/**
	 * 接收人
	 */
	@NotEmpty(message = "邮件接收人不能为空", groups = EmailValidGroups.Send.class)
	private List<String> recipients;
	/**
	 * 发送时间
	 */
	private LocalDateTime sentDate;
	/**
	 * 接收时间
	 */
	private LocalDateTime receivedDate;
	/**
	 * 内容
	 */
	@NotBlank(message = "邮件内容不能为空", groups = EmailValidGroups.Send.class)
	private String content;

	/**
	 * 附件ID
	 * key=文件id
	 * value=文件名称
	 */
	private Map<String, String> files;

	public boolean send(EmailConfig emailConfig) {
		if (recipients.isEmpty()) {
			return false;
		}
		// 根据配置设置发送人
		this.setFromAddress(List.of(emailConfig.getUsername()));
		this.receivedDate = LocalDateTime.now();
		this.setSentDate(this.receivedDate);
		boolean sendFlag = emailRecordGateway.sendByConfig(this, emailConfig);
		if (!sendFlag) {
			// 发送失败,返回false
			return false;
		}
		return emailRecordGateway.save(this);
	}
}
