package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.sync.EmailSyncState;
import com.l7bug.message.infrastructure.mapstruct.EmailSyncStateDoMapstruct;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class EmailSyncStateGatewayImplTest {

	private final Faker faker = new Faker();
	@Autowired
	private EmailSyncStateDoMapstruct emailSyncStateDoMapstruct;
	private EmailSyncState domain;

	@BeforeEach
	void setUp() {
		this.domain = emailSyncStateDoMapstruct.createDomain();
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void save() {
		Assertions.assertThatThrownBy(domain::save)
			.message()
			.satisfies(log::info)
			.isNotBlank()
		;
		domain.setUsername(faker.internet().emailAddress());
		domain.setFolder(faker.name().fullName());
		domain.setUid(faker.number().randomNumber());
		domain.setUidValidity(faker.number().randomNumber());
		domain.save();
		Assertions.assertThat(domain.getId())
			.satisfies(temp -> log.info("id: {}", temp))
			.isNotNull();
		Long id = domain.getId();
		domain.setUsername(faker.internet().emailAddress());
		domain.save();
		Assertions.assertThat(domain.getId()).isEqualTo(id);
	}
}
