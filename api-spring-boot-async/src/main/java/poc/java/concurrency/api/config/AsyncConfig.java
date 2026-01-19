package poc.java.concurrency.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AsyncConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

  private static final int CORE_POOL_SIZE = 32;
  private static final int MAX_POOL_SIZE = 48;
  private static final int KEEP_ALIVE_TIME = 24;
  private static final int QUEUE_CAPACITY = 500;

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    // final int cpus = Runtime.getRuntime().availableProcessors();
    // LOGGER.info("Platform CPUS: {}", cpus);

    // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // executor.setCorePoolSize(CORE_POOL_SIZE);
    // executor.setMaxPoolSize(MAX_POOL_SIZE);
    // executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
    // executor.setQueueCapacity(QUEUE_CAPACITY);
    // executor.setThreadNamePrefix("ResourceAnswerLookup-");
    // executor.setWaitForTasksToCompleteOnShutdown(true);
    // executor.setAwaitTerminationSeconds(60);
    // executor.initialize();
    // return executor;
    return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
  }

  @Bean
  public ExecutorService executorService() {
    // return new ThreadPoolExecutor(
    //     CORE_POOL_SIZE,
    //     MAX_POOL_SIZE,
    //     KEEP_ALIVE_TIME,
    //     TimeUnit.SECONDS,
    //     new LinkedBlockingQueue<>()
    // );
    return Executors.newVirtualThreadPerTaskExecutor();

  }
}
