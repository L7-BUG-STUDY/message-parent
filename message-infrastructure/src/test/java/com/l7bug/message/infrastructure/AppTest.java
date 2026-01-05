package com.l7bug.message.infrastructure;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class AppTest {

	@Test
	void main() throws IOException {
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/order.txt")) {
			if (resourceAsStream == null) {
				return;
			}
			byte[] bytes = resourceAsStream.readAllBytes();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("(");
			int index = 0;
			for (String item : new String(bytes).split("\n")) {
				if (index != 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append("'").append(item).append("'");
				index++;
			}
			stringBuilder.append(")");
			System.err.println(stringBuilder);
		}
	}

	@Test
	void main2() throws IOException {
		try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/ids.txt")) {
			if (resourceAsStream == null) {
				return;
			}
			byte[] bytes = resourceAsStream.readAllBytes();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("(");
			int index = 0;
			for (String item : new String(bytes).split("\n")) {
				if (index != 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(item);
				index++;
			}
			stringBuilder.append(")");
			System.err.println(stringBuilder);
		}
	}
}
