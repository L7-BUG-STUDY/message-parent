package com.l7bug.message.infrastructure.gateway;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.l7bug.common.error.RemoteErrorCode;
import com.l7bug.common.exception.RemoteException;
import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailConfigGateway;
import com.l7bug.message.domain.email.record.EmailRecord;
import com.l7bug.message.infrastructure.dao.dataobject.EmailConfigDo;
import com.l7bug.message.infrastructure.dao.repository.EmailConfigRepository;
import com.l7bug.message.infrastructure.mapstruct.EmailConfigDoMapstruct;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * MailConfigGateway
 *
 * @author Administrator
 * @since 2025/12/23 15:36
 */
@Slf4j
@Component
@AllArgsConstructor
public class EmailConfigGatewayImpl implements EmailConfigGateway {
	private static final Set<String> BREAK_FOLDER_NAME = new HashSet<>(Arrays.asList("Junk", "Drafts", "Deleted Messages", "Sent Messages"));
	private final EmailConfigRepository emailConfigRepository;

	private final EmailConfigDoMapstruct emailConfigDoMapstruct;

	public JavaMailSenderImpl buildSender(EmailConfig emailConfig) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setUsername(emailConfig.getUsername());
		sender.setPassword(emailConfig.getPassword());
		sender.setDefaultEncoding("UTF-8");
		Properties props = sender.getJavaMailProperties();
		props.put("mail.smtp.host", emailConfig.getType().getSmtpHost());
		props.put("mail.smtp.port", emailConfig.getType().getSmtpPort());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		// 如果端口是 465，通常需要开启 SSL
		// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// imap
		props.put("mail.imap.host", emailConfig.getType().getImapHost());
		props.put("mail.imap.port", emailConfig.getType().getImapPort());
		props.put("mail.imap.auth", "true");
		props.put("mail.imap.ssl.enable", "true");
		// props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		return sender;
	}

	@Override
	public boolean save(EmailConfig emailConfig) {
		EmailConfigDo emailConfigDo = emailConfigDoMapstruct.mapDo(emailConfig);
		this.emailConfigRepository.save(emailConfigDo);
		emailConfig.setId(emailConfigDo.getId());
		return true;
	}

	@Override
	public Optional<String> testConnection(EmailConfig emailConfig) {
		// 构建邮件发送器
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		try {
			// 测试连接
			javaMailSender.testConnection();
			// 连接成功返回空字符串
			return Optional.empty();
		} catch (MessagingException e) {
			// 记录连接失败的错误日志
			log.error("邮件服务连接失败,原因", e);
			// 连接失败返回错误信息
			return Optional.of(e.getMessage());
		}
	}

	@Override
	public Optional<EmailConfig> findById(Long id) {
		return emailConfigRepository.findById(id).map(emailConfigDoMapstruct::mapDomain);
	}

	@Override
	public void sendMessage(EmailConfig emailConfig, EmailRecord record, boolean canFileZip) throws Exception {
		var testConnection = emailConfig.testConnection();
		if (testConnection.isPresent()) {
			throw new RemoteException(RemoteErrorCode.EMAIL_CLIENT_ERROR);
		}
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		MimeMessage message = javaMailSender.createMimeMessage();
		// 2. 使用 MimeMessageHelper，第二个参数 true 表示需要支持 multipart（附件、HTML）
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setFrom(emailConfig.getUsername());
		helper.setTo(record.getRecipients().toArray(new String[0]));
		helper.setSubject(record.getSubject());

		// 3. 设置 HTML 内容
		// 第二个参数 true 表示发送的是 HTML，如果是 false 则会被当做纯文本显示 HTML 源码
		helper.setText(record.getContent(), true);

		if (!record.getFiles().isEmpty()) {
			// 4. 添加附件
			if (canFileZip) {
				try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8)) {
					zipOutputStream.setLevel(9);
					for (Map.Entry<String, InputStream> entry : record.getFiles().entrySet()) {
						String fileName = entry.getKey();
						InputStream is = entry.getValue();
						try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(is.readAllBytes())) {
							ZipEntry zipEntry = new ZipEntry(fileName);
							zipOutputStream.putNextEntry(zipEntry);
							byteArrayInputStream.transferTo(zipOutputStream);
							zipOutputStream.closeEntry();
						}
					}
					zipOutputStream.finish();
					String encodedFileName = MimeUtility.encodeText("attachments.zip");
					helper.addAttachment(encodedFileName, new ByteArrayResource(byteArrayOutputStream.toByteArray()));
				}
			} else {
				for (Map.Entry<String, InputStream> entry : record.getFiles().entrySet()) {
					String fileName = entry.getKey();
					InputStream is = entry.getValue();
					helper.addAttachment(fileName, new ByteArrayResource(is.readAllBytes()));
				}
			}
		}

		// 5. 发送
		javaMailSender.send(message);
	}

	@Override
	public void pullNotReadMessage(EmailConfig emailConfig, BiConsumer<EmailRecord, Message> consumer) {

	}

	@Override
	public Optional<Store> getImapStore(EmailConfig emailConfig) {
		emailConfig.testConnection();
		if (!emailConfig.getConnection()) {
			return Optional.empty();
		}
		JavaMailSenderImpl javaMailSender = buildSender(emailConfig);
		Properties javaMailProperties = javaMailSender.getJavaMailProperties();
		Session session = Session.getInstance(javaMailProperties);
		try {
			Store imaps = session.getStore("imaps");
			imaps.connect(emailConfig.getType().getImapHost(), emailConfig.getUsername(), emailConfig.getPassword());
			return Optional.of(imaps);
		} catch (MessagingException e) {
			return Optional.empty();
		}
	}

	private String getContent(@Nullable Object content, BiConsumer<String, byte[]> fileBackFun) throws Exception {
		if (content == null) {
			return "";
		}
		if (content instanceof Multipart multipart) {
			int count = multipart.getCount();
			// 邮件内容builder,后续返回
			StringBuilder builder = new StringBuilder();
			StringBuilder typeBuilder = new StringBuilder();
			for (int i = 0; i < count; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				typeBuilder.append(bodyPart.getContentType());
			}
			for (int i = 0; i < count; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (typeBuilder.toString().contains("text/plain") && typeBuilder.toString().contains("text/html")) {
					// 纯文本与html同时存在,只需要处理html,纯文本跳过
					if (bodyPart.isMimeType("text/plain")) {
						continue;
					}
				}
				String disposition = bodyPart.getDisposition();
				String fileName = bodyPart.getFileName();
				if (fileName != null) {
					fileName = MimeUtility.decodeText(fileName);
				}
				if (StrUtil.isBlank(fileName)) {
					// 文件名可能在请求头里
					String[] header = bodyPart.getHeader("Content-Type");
					if (header != null) {
						for (String s : header) {
							if (s.contains("name=")) {
								fileName = s.split("name=")[1].replace("\"", "").trim();
								break;
							}
							if (s.contains("name*=")) {
								fileName = s.split("name\\*=")[1].replace("\"", "").trim();
								break;
							}
						}
						if (StrUtil.isNotBlank(fileName)) {
							fileName = URLUtil.decode(fileName);
							fileName = fileName.replace("UTF-8''", "").replace("utf-8''", "");
						}
					}
				}
				// 处理文件可能存在的路径问题,如:D://1.txt,/home/user1/1.txt
				fileName = Optional.ofNullable(fileName)
					.map(temp -> temp.split("/")[temp.split("/").length - 1])
					.map(temp -> temp.split("\\\\")[temp.split("\\\\").length - 1])
					.orElse("");
				if (StrUtil.isAllNotBlank(disposition, fileName) && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
					// 文件名存在,开始处理文件
					try (InputStream inputStream = bodyPart.getInputStream()) {
						// 读取文件,最后调用回调函数,外部处理
						byte[] fileBytes = IoUtil.readBytes(inputStream, false);
						fileBackFun.accept(fileName, fileBytes);
					}
					continue;
				}
				builder.append(getContent(bodyPart, fileBackFun));
			}
			return builder.toString();
		} else if (content instanceof String) {
			return content.toString();
		}
		if (content instanceof BodyPart bodyPart) {
			return getContent(bodyPart.getContent(), fileBackFun);
		} else {
			return "";
		}
	}

	/**
	 * 获取IMAP存储中所有叶子节点文件夹
	 * 该方法会递归遍历IMAP存储的文件夹结构，获取所有最深层级的邮件文件夹（叶子节点）
	 * 对于特定名称的文件夹（如垃圾邮件、草稿等）会跳过处理
	 *
	 * @param imapStore IMAP存储对象，包含邮件服务器的连接和文件夹结构
	 * @return 包含所有叶子节点文件夹的列表，如果出现异常则返回空列表
	 */
	private List<Folder> getAllLeafNodeByImap(Store imapStore) {
		try {
			// 从默认文件夹开始递归获取所有叶子节点
			return getAllLeafNode(imapStore.getDefaultFolder());
		} catch (Exception e) {
			// 出现异常时返回空列表，避免程序崩溃
			return new ArrayList<>();
		}
	}

	/**
	 * 递归获取文件夹的所有叶子节点
	 * 该方法会递归遍历文件夹结构，获取所有最深层级的邮件文件夹（叶子节点）
	 * 对于特定名称的文件夹（如垃圾邮件、草稿等）会跳过处理
	 *
	 * @param folder 需要遍历的邮件文件夹
	 * @return 包含所有叶子节点文件夹的列表
	 * @throws Exception 当访问邮件文件夹时可能抛出的异常
	 */
	private List<Folder> getAllLeafNode(Folder folder) throws Exception {
		// 获取当前文件夹的子文件夹列表
		Folder[] list = folder.list();
		if (list == null || list.length == 0) {
			// 如果当前文件夹没有子文件夹，检查是否为需要跳过的文件夹类型
			if (BREAK_FOLDER_NAME.contains(folder.getFullName())) {
				return Collections.emptyList();
			}
			// 如果不是需要跳过的文件夹，则返回当前文件夹作为叶子节点
			return Collections.singletonList(folder);
		}
		// 递归处理所有子文件夹
		List<Folder> result = new LinkedList<>();
		for (Folder item : list) {
			result.addAll(getAllLeafNode(item));
		}
		return result;
	}
}
