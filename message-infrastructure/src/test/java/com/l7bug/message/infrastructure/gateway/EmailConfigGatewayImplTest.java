package com.l7bug.message.infrastructure.gateway;

import com.alibaba.fastjson2.JSONObject;
import com.l7bug.message.domain.email.EmailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailConfigGatewayImplTest {
	@Autowired
	private EmailConfigGatewayImpl emailConfigGateway;

	@Test
	void testConnection() {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		String password = emailConfig.getPassword();
		emailConfig.setPassword("123321");
		assertThat(emailConfig.testConnection()).isNotEmpty();
		assertThat(emailConfig.getConnection()).isFalse();
		emailConfig.setPassword(password);
		assertThat(emailConfig.testConnection()).isEmpty();
		assertThat(emailConfig.getConnection()).isTrue();
		byId = this.emailConfigGateway.findById(UUID.randomUUID().getMostSignificantBits());
		assertThat(byId).isNotNull().isEmpty();
	}

	@Test
	void testSendMessage() throws IOException {
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		EmailConfig emailConfig = byId.get();
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/testEmail.html"); InputStream log = this.getClass().getResourceAsStream("/test.txt");) {
			if (resourceAsStream == null) {
				return;
			}
			if (log == null) {
				return;
			}
			byte[] htmlBytes = resourceAsStream.readAllBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 10000; i++) {
				sb.append(UUID.randomUUID());
			}
			try (ByteArrayInputStream htmlByteArrayIS = new ByteArrayInputStream(htmlBytes); ByteArrayInputStream sbIS = new ByteArrayInputStream(sb.toString().getBytes())) {
				String content = new String(htmlBytes, StandardCharsets.UTF_8);
				boolean b = emailConfig.sendMessage("测试", content, Map.of("test.txt", log, "testEmail.html", htmlByteArrayIS, "uuids.txt", sbIS), true, "ll789y@gmail.com", "l7-bug@qq.com");
				assertThat(b).isTrue();
			}
		}
	}

	@Test
	void sendFlightChangeEmail() throws IOException {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Optional<EmailConfig> byId = this.emailConfigGateway.findById(1L);
		assertThat(byId).isNotNull();
		if (byId.isEmpty()) {
			return;
		}
		JSONObject flightJson = new JSONObject();
		flightJson.put("orderNo", UUID.randomUUID().toString());
		flightJson.put("type", "delay");
		flightJson.put("pnr", UUID.randomUUID().toString().replace("-", "").substring(0, 6));
		flightJson.put("ticketNo", UUID.randomUUID().toString().replace("-", "").substring(0, 12));
		flightJson.put("platform", "goms");
		flightJson.put("notifyTime", LocalDateTime.now().format(dateTimeFormatter));
		flightJson.put("airlineCode", UUID.randomUUID().toString().substring(0, 2).toUpperCase());
		flightJson.put("flightNo", flightJson.getString("airlineCode") + UUID.randomUUID().toString().hashCode());
		flightJson.put("dptDate", LocalDate.now().plusWeeks(1).toString());
		flightJson.put("dptTime", "00:00");
		flightJson.put("dptAirPort", UUID.randomUUID().toString().substring(0, 3).toUpperCase());
		flightJson.put("arrDate", LocalDate.now().plusWeeks(1).toString());
		flightJson.put("arrTime", "06:00");
		flightJson.put("arrAirPort", UUID.randomUUID().toString().substring(0, 3).toUpperCase());
		flightJson.put("newAirlineCode", UUID.randomUUID().toString().substring(0, 2).toUpperCase());
		flightJson.put("newFlightNo", flightJson.getString("newAirlineCode") + UUID.randomUUID().toString().hashCode());
		flightJson.put("newDptDate", LocalDate.now().plusWeeks(1).plusDays(1).toString());
		flightJson.put("newDptTime", "12:00");
		flightJson.put("newDptAirPort", UUID.randomUUID().toString().substring(0, 3).toUpperCase());
		flightJson.put("newArrDate", LocalDate.now().plusWeeks(1).plusDays(1).toString());
		flightJson.put("newArrTime", "18:00");
		flightJson.put("newArrAirPort", UUID.randomUUID().toString().substring(0, 3).toUpperCase());
		EmailConfig emailConfig = byId.get();
		try (ByteArrayInputStream in = new ByteArrayInputStream(flightJson.toJSONString().getBytes(StandardCharsets.UTF_8))) {
			emailConfig.sendMessage("[航变通知]::测试测试测试测试测试测试" + UUID.randomUUID(), ("<h1>航变通知!" + UUID.randomUUID() + "</h1>").repeat(100), Map.of("flight-change.json", in), false, emailConfig.getUsername());
		}
	}

}
