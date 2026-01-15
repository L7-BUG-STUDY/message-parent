package com.l7bug.consul;

import com.l7bug.common.error.RemoteErrorCode;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.file.client.factory.FileFallbackFactory;
import com.l7bug.file.client.feign.FileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MyFileFallbackFactory
 *
 * @author Administrator
 * @since 2026/1/15 15:02
 */
@Slf4j
@Component
public class MyFileFallbackFactory extends FileFallbackFactory {
	@Override
	public FileClient create(Throwable cause) {
		return new FileClient() {
			@Override
			public Result<String> index() {
				log.debug("[my]::", cause);
				Result<String> failure = Results.failure(RemoteErrorCode.FALLBACK.getCode(), cause.getMessage());
				failure.setData(cause.getMessage());
				return failure;
			}
		};
	}
}
