package com.l7bug.message.domain.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * EmailType
 *
 * @author Administrator
 * @since 2025/12/23 12:29
 */
@AllArgsConstructor
@Getter
public enum EmailType {
	/**
	 * 企业微信
	 */
	WX_WORK("imap.exmail.qq.com", 993, "smtp.exmail.qq.com", 465),
	QQ("imap.qq.com", 993, "smtp.qq.com", 465),

	;
	private final String imapHost;
	private final int imapPort;
	private final String smtpHost;
	private final int smtpPort;
}
