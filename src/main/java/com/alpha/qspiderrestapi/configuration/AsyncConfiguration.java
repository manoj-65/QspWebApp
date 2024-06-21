package com.alpha.qspiderrestapi.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
//@EnableAsync(proxyTargetClass = true)
public class AsyncConfiguration {

//	@Bean("async")
//	public Executor asyncTaskExecutor() {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(4);
//		taskExecutor.setQueueCapacity(150);
//		taskExecutor.setMaxPoolSize(4);
//		taskExecutor.setThreadNamePrefix("MAIL-SENDER");
//		taskExecutor.initialize();
//		return taskExecutor;
//	}
}
