package hu.bme.aut.liftservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "lift-service.generator")
public class ServiceConfiguration {
    private int capacity;
    private int maxWaiters;
    private int maxNewWaiters;
    private int maxWaitersLeaving;
    private int maxPeopleGettingOffAtOnce;
    private int maxPeopleGettingOnAtOnce;
    private int percentageOfStartingWaiters;

    @Bean
    public Random createRandom() {
        return new Random();
    }

    public int getCurrentlyWaiting() {
        return capacity * percentageOfStartingWaiters / 100;
    }
}
