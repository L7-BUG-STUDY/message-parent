package com.l7bug.consul;

import com.l7bug.common.error.RemoteErrorCode;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.consul.feign.FileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * FileFallbackFactory
 *
 * @author Administrator
 * @since 2026/1/15 13:55
 */
@Component
@Slf4j
public class FileFallbackFactory implements FallbackFactory<FileClient> {
	@Override
	public FileClient create(Throwable cause) {
		return new DefaultFileClientFallback(cause);
	}

	public record DefaultFileClientFallback(Throwable cause) implements FileClient {

		@Override
		public Result<String> index() {
			log.debug("[服务默认降级]::{}", cause.getMessage());
			return Results.failure(RemoteErrorCode.FALLBACK);
		}
	}
}
