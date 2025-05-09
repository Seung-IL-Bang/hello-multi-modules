package io.hello.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Set;

@Slf4j
public class CommandLineOptionArgsV2 {

    // java -jar app.jar dataA dataB --url=devdb --username=dev_user --password=dev_pw "hello world" mode=on
    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }

        /**
         * Using the ApplicationArguments interface provided by Spring and the DefaultApplicationArguments implementation
         * allows command line option arguments to be parsed in a canonical way for convenience
         */
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("SourceArgs = {}", List.of(appArgs.getSourceArgs()));
        log.info("NonOptionArgs = {}", appArgs.getNonOptionArgs());
        log.info("OptionNames = {}", appArgs.getOptionNames());

        Set<String> optionNames = appArgs.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option args {}={}", optionName,
                    appArgs.getOptionValues(optionName));
        }
        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        List<String> mode = appArgs.getOptionValues("mode");
        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
        log.info("mode={}", mode);
    }
}
