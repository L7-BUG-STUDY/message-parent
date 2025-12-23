package com.l7bug.message.infrastructure.dao.repository;

import com.l7bug.message.infrastructure.dao.dataobject.EmailConfigDo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EmailConfigRepositoryTest {
	@Autowired
	private EmailConfigRepository emailConfigRepository;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testSelect() {
		List<EmailConfigDo> all = emailConfigRepository.findAll();
		Assertions.assertThat(all)
			.isNotNull();
		System.out.println(all);
	}
}
