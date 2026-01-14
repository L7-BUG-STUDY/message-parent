package com.l7bug.consul;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConsulFixConfig {

	@Bean
	public RestClientCustomizer restClientCustomizer(JsonMapper jsonMapper) {
		return restClientBuilder -> {
			// 1. 创建 Spring 7 推荐的转换器
			JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter(jsonMapper);

			// 2. 核心补丁：强制让它支持 text/html。
			// 因为 Consul 在长轮询超时或 404 时会返回 text/html，这是你报错的根源。
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			mediaTypes.add(MediaType.TEXT_HTML);
			mediaTypes.add(MediaType.valueOf("text/html;charset=UTF-8"));
			converter.setSupportedMediaTypes(mediaTypes);

			// 3. 在 Spring 7 中，使用 addCustomConverter 注入自定义转换器
			// 它会被 RestClient 优先用于匹配响应内容
			restClientBuilder.configureMessageConverters(converters -> {
				converters.addCustomConverter(converter);
			});
		};
	}
}
