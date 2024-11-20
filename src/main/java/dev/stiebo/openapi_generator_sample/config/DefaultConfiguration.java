package dev.stiebo.openapi_generator_sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultConfiguration {
    @Bean
    public Long defaultMaxAllowed() {
        return 200L;
    }

    @Bean
    public Long defaultMaxManual() {
        return 1500L;
    }
}
