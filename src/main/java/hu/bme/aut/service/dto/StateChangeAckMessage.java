package hu.bme.aut.service.dto;


import hu.bme.aut.service.dto.LiftStateMessage.LiftState;

import java.time.Instant;

public record StateChangeAckMessage(String liftId, LiftState state, Instant timestamp) {
}
