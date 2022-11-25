package hu.bme.aut.controllerservice.service.dto;


import hu.bme.aut.controllerservice.service.dto.LiftStateMessage.LiftState;

import java.time.Instant;

public record StateChangeAckMessage(String liftId, LiftState state, Instant timestamp) {
}
