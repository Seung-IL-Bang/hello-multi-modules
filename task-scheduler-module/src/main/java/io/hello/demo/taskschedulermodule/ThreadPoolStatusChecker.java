package io.hello.demo.taskschedulermodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ThreadPoolStatusChecker {

    private final Map<String, TaskScheduler> taskSchedulerMap;
    Logger log = LoggerFactory.getLogger(ThreadPoolStatusChecker.class);

    public ThreadPoolStatusChecker(Map<String, TaskScheduler> taskSchedulerMap) {
        this.taskSchedulerMap = taskSchedulerMap;
    }

    public void printThreadPoolStatus() {
        // 현재 스레드 이름 가져오기
        String currentThreadName = Thread.currentThread().getName();

        // 스레드 이름에서 접두사 추출
        String threadPrefix = extractThreadPrefix(currentThreadName);

        // 적절한 스케줄러 찾기
        TaskScheduler scheduler = findSchedulerByThreadPrefix(threadPrefix);

        if (scheduler instanceof ThreadPoolTaskScheduler) {
            ThreadPoolExecutor executor = ((ThreadPoolTaskScheduler) scheduler).getScheduledThreadPoolExecutor();

            int activeCount = executor.getActiveCount();
            int poolSize = executor.getPoolSize();
            int queueSize = executor.getQueue().size();

            log.info("Thread Pool [{}] - Active Threads: {}, Pool Size: {}, Queue Size: {}",
                    threadPrefix, activeCount, poolSize, queueSize);
        } else {
            log.info("No matching scheduler found for thread: {}", currentThreadName);
        }
    }

    private String extractThreadPrefix(String threadName) {
        // 스레드 번호 접미사 추출 (예: "custom-task-scheduler1" -> "custom-task-scheduler")
        return threadName.substring(0, threadName.length() - 1);
    }

    private TaskScheduler findSchedulerByThreadPrefix(String threadPrefix) {
        // 각 스케줄러의 스레드 접두사를 확인하여 일치하는 스케줄러 찾기
        for (TaskScheduler scheduler : taskSchedulerMap.values()) {
            if (scheduler instanceof ThreadPoolTaskScheduler poolScheduler) {
                String prefix = poolScheduler.getThreadNamePrefix();
                if (threadPrefix.startsWith(prefix)) {
                    return scheduler;
                }
            }
        }
        return null;
    }
}
