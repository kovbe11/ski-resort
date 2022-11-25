package hu.bme.aut.liftservice.service.dto;

import hu.bme.aut.liftservice.service.StateGeneratorService.LiftState;

import java.time.Instant;

public record StateChangeAckMessage(String liftId, LiftState state, Instant timestamp) {
    public StateChangeAckMessage(String liftId, LiftState state) {
        this(liftId, state, Instant.now());
    }
}
