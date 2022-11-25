package hu.bme.aut.liftservice.service.dto;

import hu.bme.aut.liftservice.service.StateGeneratorService.LiftState;

public record NewLiftStateMessage(LiftState liftState) {
}
