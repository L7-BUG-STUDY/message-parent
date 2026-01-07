package com.l7bug.message.infrastructure.dao.repository;


import cn.hutool.core.util.IdUtil;
import com.l7bug.message.domain.email.record.Type;
import com.l7bug.message.infrastructure.dao.dataobject.EmailRecordDo;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EmailRecordRepositoryTest {
	private final Faker FAKER = new Faker();
	private final Faker FAKER_CN = new Faker(Locale.CHINA);
	@Autowired
	private EmailRecordRepository emailRecordRepository;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void test() {
		List<EmailRecordDo> entities = new ArrayList<>(10);
		for (int i = 0; i < 100; i++) {
			EmailRecordDo entity = new EmailRecordDo();
			entity.setType(Type.SEND.name());
			entity.setFolder(FAKER.name().name());
			entity.setSubject(FAKER.business().creditCardType());
			entity.setFormAddress(List.of(FAKER.internet().emailAddress()));
			entity.setRecipients(List.of(FAKER.internet().emailAddress(), FAKER.internet().emailAddress(), FAKER.internet().emailAddress()));
			entity.setSendDate(FAKER.timeAndDate().past(10, TimeUnit.DAYS).atOffset(ZoneOffset.ofHours(8)));
			entity.setReceivedDate(OffsetDateTime.now());
			entity.setContent(FAKER_CN.name().name());
			entity.setFiles(Map.of(IdUtil.getSnowflakeNextIdStr(), fileName(), IdUtil.getSnowflakeNextIdStr(), fileName()));
			entities.add(entity);
		}
		emailRecordRepository.saveAll(entities);
	}

	private String fileName() {
		return FAKER.file().fileName(null, null, null, "/");
	}
}
