package com.l7bug.message.infrastructure.dao.dataobject;

import com.l7bug.database.dataobject.BaseDo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * EmailRecordDo
 *
 * @author Administrator
 * @since 2026/1/7 11:49
 */
@Getter
@Setter
@Entity
@Table(name = "message_email_record")
public class EmailRecordDo extends BaseDo {

	@Serial
	private static final long serialVersionUID = -342773148603870688L;

	private String type;

	private String folder;

	private String subject;

	@JdbcTypeCode(SqlTypes.JSON)
	private List<String> formAddress;

	@JdbcTypeCode(SqlTypes.JSON)
	private List<String> recipients;

	private OffsetDateTime sendDate;

	private OffsetDateTime receivedDate;

	private String content;

	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> files;
}
