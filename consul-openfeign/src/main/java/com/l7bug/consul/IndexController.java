package com.l7bug.consul;

import cn.hutool.core.io.IoUtil;
import com.l7bug.common.result.Result;
import com.l7bug.consul.feign.DemoClient;
import com.l7bug.file.client.FileClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * IndexController
 *
 * @author Administrator
 * @since 2026/1/14 11:22
 */
@AllArgsConstructor
@RestController
public class IndexController {
	private final DemoClient demoClient;

	private final FileClient fileClient;

	@GetMapping
	public String hello() {
		return "hello world" + fileClient.hello().getData();
	}

	@GetMapping("/image")
	public void image(HttpServletResponse response) throws IOException {
		Result<byte[]> download = this.fileClient.download(1L);
		IoUtil.copy(new ByteArrayInputStream(download.getData()), response.getOutputStream());
	}
}
