package com.l7bug.gateway.filter;

import com.l7bug.common.etc.SystemEtc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpMethod method = request.getMethod();
		long startTime = System.currentTimeMillis();

		AtomicReference<String> requestId = new AtomicReference<>();
		requestId.set(exchange.getRequest().getHeaders().getFirst(SystemEtc.REQUEST_ID));
		String requestIdAttributes = exchange.getAttributes().getOrDefault(SystemEtc.REQUEST_ID, "").toString();
		ServerHttpRequest.Builder requestBuild = exchange.getRequest().mutate();
		if (!StringUtils.hasText(requestId.get()) || !StringUtils.hasText(requestIdAttributes)) {
			String uuid = UUID.randomUUID().toString();
			requestId.set(uuid);
			requestBuild.header(SystemEtc.REQUEST_ID, requestId.get());
			exchange.getAttributes().put(SystemEtc.REQUEST_ID, requestId.get());
		}

		log.info("请求ID: {}", requestId.get());
		log.info("请求URI: {}", request.getURI());
		log.info("请求类型: {}", method);
		log.info("请求头: {}", request.getHeaders());

		if (method == HttpMethod.GET) {
			log.info("请求参数: {}", request.getQueryParams());
		}
		ServerWebExchange newExchange = exchange.mutate()
			.request(requestBuild.build())
			.build();
		return chain.filter(newExchange).then(Mono.fromRunnable(() -> {
			long duration = System.currentTimeMillis() - startTime;
			log.info("响应时间：{} ms", duration);
		}));
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
