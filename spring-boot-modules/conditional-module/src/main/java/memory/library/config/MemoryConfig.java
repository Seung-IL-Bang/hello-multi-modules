package memory.library.config;

import memory.library.MemoryController;
import memory.library.MemoryFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Conditional(MemoryCondition.class)
//@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev", matchIfMissing = true)
//@ConditionalOnBean(MemoryController.class)
//@ConditionalOnMissingBean(MemoryController.class)
//@ConditionalOnWebApplication
//@ConditionalOnNotWebApplication
public class MemoryConfig {

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }
}

