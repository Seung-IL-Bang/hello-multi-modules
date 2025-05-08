package io.hello.demo.config;

import io.micrometer.common.util.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MemoryCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        String profile = context.getEnvironment().getProperty("spring.profiles.active");

        if (StringUtils.isNotBlank(profile) && profile.equals("dev")) {
            return true;
        }

        return false;
    }
}
