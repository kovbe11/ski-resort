package hu.bme.aut.controllerservice.service.dto;

import java.time.Instant;

public record LiftDisconnectionMessage(String liftId, Instant timestamp) {
    public LiftDisconnectionMessage(String liftId) {
        this(liftId, Instant.now());
    }
}
