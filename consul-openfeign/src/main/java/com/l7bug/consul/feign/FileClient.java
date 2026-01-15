package com.l7bug.consul.feign;

import com.l7bug.common.result.Result;
import com.l7bug.consul.FileFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FileClient
 *
 * @author Administrator
 * @since 2026/1/15 13:56
 */
@FeignClient(value = "file-service", fallbackFactory = FileFallbackFactory.class)
public interface FileClient {

	@GetMapping("/hello")
	Result<String> index();
}
