package com.l7bug.message.infrastructure.dao.dataobject;

import com.l7bug.database.dataobject.BaseDo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * EmailConfig
 *
 * @author Administrator
 * @since 2025/12/23 14:49
 */
@Getter
@Setter
@Entity
@Table(name = "message_email_config")
public class EmailConfigDo extends BaseDo {
	@Serial
	private static final long serialVersionUID = -6021669100033600195L;
	private String type;
	private String username;
	private String password;
	private Boolean connection = false;
}
