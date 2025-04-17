package io.hello.demo.taskschedulermodule;

import io.hello.demo.utilsmodule.ThreadUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    @Scheduled(fixedRate = 500)
    public void scheduleFixedRateTask() {
        System.out.println("Running in thread: " + Thread.currentThread().getName());
        ThreadUtils.sleep(5000);
    }
}
