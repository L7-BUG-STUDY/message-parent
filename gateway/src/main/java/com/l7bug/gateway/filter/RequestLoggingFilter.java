package com.l7bug.gateway.filter;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.etc.Headers;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.gateway.AuthConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RequestLoggingFilter
 *
 * @author Administrator
 * @since 2026/1/16 14:09
 */
@Slf4j
@Component
@AllArgsConstructor
public class RequestLoggingFilter implements GlobalFilter, Ordered {

	private final StringRedisTemplate stringRedisTemplate;
	private final AuthConfiguration.AuthProperties authProperties;
	private final JsonMapper jsonMapper;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpMethod method = request.getMethod();
		long startTime = System.currentTimeMillis();

		AtomicReference<String> requestId = new AtomicReference<>();
		requestId.set(exchange.getRequest().getHeaders().getFirst(Headers.REQUEST_ID));
		String requestIdAttributes = exchange.getAttributes().getOrDefault(Headers.REQUEST_ID, "").toString();
		ServerHttpRequest.Builder requestBuild = exchange.getRequest().mutate();
		if (!StringUtils.hasText(requestId.get()) || !StringUtils.hasText(requestIdAttributes)) {
			String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);
			requestId.set(uuid);
			requestBuild.header(Headers.REQUEST_ID, requestId.get());
			exchange.getAttributes().put(Headers.REQUEST_ID, requestId.get());
		}
		boolean canAuth = true;
		for (String s : authProperties.getWhiteApi()) {
			if (request.getPath().toString().endsWith(s)) {
				canAuth = false;
				break;
			}
		}
		// request.getPath().toString().endsWith()
		log.info("请求ID: {}", requestId.get());
		log.info("请求URI: {}", request.getURI());
		log.info("请求类型: {}", method);
		log.info("请求头: {}", request.getHeaders());

		if (method == HttpMethod.GET) {
			log.info("请求参数: {}", request.getQueryParams());
		}
		if (canAuth) {
			String first = exchange.getRequest().getHeaders().getFirst(Headers.TOKEN);
			HashOperations<String, Object, Object> stringObjectObjectHashOperations = this.stringRedisTemplate.opsForHash();
			String authKey = "system:user:token:" + first;
			Map<Object, Object> entries = stringObjectObjectHashOperations.entries(authKey);
			if (!entries.isEmpty()) {
				this.stringRedisTemplate.expire(authKey, 2, TimeUnit.HOURS);
				String username = entries.getOrDefault("username", "").toString();
				String userId = entries.getOrDefault("id", "").toString();
				requestBuild.header(Headers.USERNAME, username);
				requestBuild.header(Headers.USER_ID, userId);
				requestBuild.header(Headers.AUTHORITIES, entries.getOrDefault("authorities", "").toString());
			} else {
				log.warn("用户未登录");
				return renderJson(exchange.mutate().request(requestBuild.build()).build(), HttpStatus.UNAUTHORIZED, Results.failure(ClientErrorCode.NOT_AUTHENTICATION));
			}
		}
		ServerWebExchange newExchange = exchange.mutate()
			.request(requestBuild.build())
			.build();
		return chain.filter(newExchange).then(Mono.fromRunnable(() -> {
			long duration = System.currentTimeMillis() - startTime;
			log.info("响应时间：{} ms", duration);
		}));
	}

	private Mono<Void> renderJson(ServerWebExchange exchange, HttpStatus status, Result<?> result) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		byte[] bytes = jsonMapper.writeValueAsBytes(result);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
