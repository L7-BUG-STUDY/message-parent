package com.l7bug.message.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * MessageRecordDdl
 *
 * @author Administrator
 * @since 2026/1/7 11:26
 */
@SpringBootTest
public class MessageRecordDdl {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void createTable() throws IOException {
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/table-record.sql")) {
			if (resourceAsStream == null) {
				return;
			}
			String[] split = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8).split(";");
			for (String s : split) {
				jdbcTemplate.execute(s);
			}
		}
	}
}
