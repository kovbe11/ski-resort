package hu.bme.aut.liftservice.service.dto;


import hu.bme.aut.liftservice.service.StateGeneratorService.LiftState;

import java.time.Instant;

public record LiftStateMessage(String liftId, int currentlyOnLift, int currentlyWaitingForLift, LiftState state,
                               Instant timestamp) {

    public LiftStateMessage(String liftId, int currentlyOnLift, int currentlyWaitingForLift, LiftState state) {
        this(liftId, currentlyOnLift, currentlyWaitingForLift, state, Instant.now());
    }

}
