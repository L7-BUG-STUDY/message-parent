package com.l7bug.consul;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * DemoProperties
 *
 * @author Administrator
 * @since 2026/1/14 10:37
 */
@RefreshScope
@Data
@Component
@EnableConfigurationProperties(DemoProperties.class)
@ConfigurationProperties(prefix = "l7-bug.demo")
public class DemoProperties {
	private String username;
	private String password;
}
