package io.hello.demo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class CommandLineBean {

    // java -jar app.jar --url=devdb --username=dev_user --password=dev_pw mode=on
    private final ApplicationArguments args;

    public CommandLineBean(ApplicationArguments args) {
        this.args = args;
    }

    @PostConstruct
    public void init() {
        log.info("source {}", List.of(args.getSourceArgs()));
        Set<String> optionNames = args.getOptionNames();
        log.info("optionNames {}", optionNames);
        for (String optionName : optionNames) {
            log.info("option args {}={}", optionName, args.getOptionValues(optionName));
        }
    }
}
