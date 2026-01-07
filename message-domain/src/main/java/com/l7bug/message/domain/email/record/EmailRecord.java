package com.l7bug.message.domain.email.record;

import com.l7bug.message.domain.email.EmailValidGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.InputStream;
import java.time.OffsetDateTime;
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
	private String messageId;
	/**
	 * 邮件记录类型
	 */
	private Type type;
	/**
	 * 文件夹
	 */
	private String folder = "";
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
	private List<String> recipients = List.of();
	/**
	 * 发送时间
	 */
	private OffsetDateTime sentDate;
	/**
	 * 接收时间
	 */
	private OffsetDateTime receivedDate;
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
	private Map<String, InputStream> sendFiles = Map.of();

	private Map<String, String> files;
}
