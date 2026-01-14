package com.l7bug.consul;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

/**
 * EmailController
 *
 * @author Administrator
 * @since 2026/1/13 16:31
 */
@AllArgsConstructor
@RestController
public class EmailController {
	private final EmailProperties emailProperties;
	private final JsonMapper jsonMapper;
	private final DemoProperties demoProperties;
	private final ServerProperties serverProperties;
	private final WebServerApplicationContext context;

	@GetMapping("/email")
	public String emailProcess() {
		return emailProperties.getUsername() + "::" + emailProperties.getPassword();
	}

	@GetMapping("/demo")
	public String demo() {
		return "[%s]::".formatted(context.getWebServer().getPort()) + demoProperties.getUsername() + "::" + demoProperties.getPassword();
	}
}
