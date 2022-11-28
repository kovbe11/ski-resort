package hu.bme.aut.service.dto;


import java.time.Instant;

public record LiftStateMessage(String liftId, int currentlyOnLift, int currentlyWaitingForLift, LiftState state,
                               Instant timestamp) {
    public enum LiftState {
        OPEN, CLOSED, TEMPORARILY_CLOSED, JUST_CONNECTED
    }
}
