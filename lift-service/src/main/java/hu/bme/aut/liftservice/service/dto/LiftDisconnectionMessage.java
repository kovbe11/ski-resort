package hu.bme.aut.liftservice.service.dto;

import java.time.Instant;

public record LiftDisconnectionMessage(String liftId, Instant timestamp) {
    public LiftDisconnectionMessage(String liftId) {
        this(liftId, Instant.now());
    }
}
