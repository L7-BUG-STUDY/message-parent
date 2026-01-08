package com.l7bug.message.domain.email;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

@Slf4j
public class Jdk25Test {

	@Test
	public void test() throws InterruptedException {
		// 模拟 1,000 个并发任务
		int taskCount = 1000;
		// 获取 CPU 核心数
		int availableProcessors = Runtime.getRuntime().availableProcessors();

		System.out.println("CPU 核心数: " + availableProcessors);
		System.out.println("任务总数: " + taskCount + " (每个任务模拟阻塞 50ms)");
		System.out.println("--------------------------------------------------");

		// 1. 测试虚拟线程
		long startVT = System.currentTimeMillis();
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < taskCount; i++) {
				executor.execute(() -> handleTask());
			}
		} // try-with-resources 会自动调用 shutdown 和 awaitTermination
		long endVT = System.currentTimeMillis();
		printResult("虚拟线程", endVT - startVT, taskCount);

		// 2. 测试普通线程池 (固定大小为 CPU 核心数)
		long startTP = System.currentTimeMillis();
		try (ExecutorService executor = new ThreadPoolExecutor(
			availableProcessors,
			availableProcessors,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>())) {
			for (int i = 0; i < taskCount; i++) {
				executor.execute(() -> handleTask());
			}
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
		}
		long endTP = System.currentTimeMillis();
		printResult("普通线程池", endTP - startTP, taskCount);
	}

	/**
	 * 模拟 IO 密集型任务
	 */
	private void handleTask() {
		try {
			// 关键：虚拟线程在 sleep 时会释放底层载体线程，而普通线程会一直占着
			Thread.sleep(50);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void printResult(String mode, long costTime, int count) {
		double throughput = (double) count / (costTime / 1000.0);
		System.err.println(mode + " 总耗时: " + costTime + " ms");
		System.err.println(mode + " 吞吐量: " + String.format("%.2f", throughput) + " tasks/s");
		System.err.println("#$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	}
}
