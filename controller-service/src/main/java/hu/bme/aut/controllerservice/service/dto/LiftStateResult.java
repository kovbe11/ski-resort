package hu.bme.aut.controllerservice.service.dto;

import hu.bme.aut.controllerservice.service.dto.LiftStateMessage.LiftState;

import java.time.Instant;

public record LiftStateResult(int currentlyOnLift,
                              int currentlyWaitingForLift,
                              LiftState state,
                              Instant timestamp) {
}
