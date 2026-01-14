package com.l7bug.consul;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Test
 *
 * @author Administrator
 * @since 2026/1/13 16:24
 */
@Configuration
@Getter
@Setter
@EnableConfigurationProperties(EmailProperties.class)
@ConfigurationProperties(prefix = "l7bug.email")
public class EmailProperties {
	private String username;
	private String password;
}
