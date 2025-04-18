package io.hello.demo.taskschedulermodule;

import io.hello.demo.utilsmodule.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduleTasks.class);

    private final ThreadPoolStatusChecker threadPoolStatusChecker;

    public ScheduleTasks(ThreadPoolStatusChecker threadPoolStatusChecker) {
        this.threadPoolStatusChecker = threadPoolStatusChecker;
    }

    @Scheduled(fixedRate = 500)
    public void scheduleFixedRateTask1() {
        log.info("Task started in : {}", Thread.currentThread().getName());
        threadPoolStatusChecker.printThreadPoolStatus();
        ThreadUtils.sleep(5000);
    }

    @Scheduled(fixedRate = 500)
    public void scheduleFixedRateTask2() {
        log.info("Task started in : {}", Thread.currentThread().getName());
        threadPoolStatusChecker.printThreadPoolStatus();
        ThreadUtils.sleep(5000);
    }

    @Scheduled(fixedRate = 500, scheduler = "customTaskScheduler")
    public void scheduleFixedRateTask3() {
        log.info("Task started in : {}", Thread.currentThread().getName());
        threadPoolStatusChecker.printThreadPoolStatus();
        ThreadUtils.sleep(5000);
    }

    @Scheduled(fixedRate = 500, scheduler = "customTaskScheduler")
    public void scheduleFixedRateTask4() {
        log.info("Task started in : {}", Thread.currentThread().getName());
        threadPoolStatusChecker.printThreadPoolStatus();
        ThreadUtils.sleep(5000);
    }
}
