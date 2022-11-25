package hu.bme.aut.controllerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

@Configuration
public class SynchronizerConfig {

    @Bean
    public HashMap<String, CompletableFuture<Instant>> completableFutureHashMap() {
        return new LinkedHashMap<>();
    }
}
