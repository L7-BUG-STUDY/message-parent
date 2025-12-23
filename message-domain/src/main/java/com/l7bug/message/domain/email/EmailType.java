package com.l7bug.message.domain.email;

import lombok.AllArgsConstructor;

/**
 * EmailType
 *
 * @author Administrator
 * @since 2025/12/23 12:29
 */
@AllArgsConstructor
public enum EmailType {
	/**
	 * 企业微信
	 */
	WX_WORK("imap.exmail.qq.com", 993, "smtp.exmail.qq.com", 465),

	;
	private final String imapHost;
	private final int imapPort;
	private final String smtpHost;
	private final int smtpPort;
}
