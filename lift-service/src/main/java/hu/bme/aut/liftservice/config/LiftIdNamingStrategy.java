package hu.bme.aut.liftservice.config;

import org.springframework.amqp.core.NamingStrategy;


public class LiftIdNamingStrategy implements NamingStrategy {

    private final String liftId;

    public LiftIdNamingStrategy(String liftId) {
        this.liftId = liftId;
    }

    @Override
    public String generateName() {
        return liftId + "-queue";
    }
}
