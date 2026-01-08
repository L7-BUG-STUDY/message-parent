package com.l7bug.message.infrastructure.dao.dataobject;

import com.l7bug.database.dataobject.BaseNotDeleDo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serial;

/**
 * EmailSyncState
 *
 * @author Administrator
 * @since 2026/1/8 17:52
 */
@Getter
@Setter
@Entity
@Table(name = "message_email_sync_state")
public class EmailSyncStateDo extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = -2388760983835729158L;
	@Size(max = 64)
	@NotNull
	@ColumnDefault("''")
	@Column(name = "username", nullable = false, length = 64)
	private String username;

	@Size(max = 64)
	@NotNull
	@Column(name = "folder", nullable = false, length = 64)
	private String folder;

	@NotNull
	@ColumnDefault("0")
	@Column(name = "uid_validity", nullable = false)
	private Long uidValidity;

	@NotNull
	@ColumnDefault("0")
	@Column(name = "uid", nullable = false)
	private Long uid;


}
