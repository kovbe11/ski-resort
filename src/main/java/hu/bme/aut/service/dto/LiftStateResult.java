package hu.bme.aut.service.dto;

import hu.bme.aut.service.dto.LiftStateMessage.LiftState;

import java.time.Instant;

public record LiftStateResult(int currentlyOnLift,
                              int currentlyWaitingForLift,
                              LiftState state,
                              Instant timestamp) {
}
