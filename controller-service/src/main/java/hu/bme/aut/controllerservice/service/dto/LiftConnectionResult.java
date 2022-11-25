package hu.bme.aut.controllerservice.service.dto;

import java.time.Instant;

public record LiftConnectionResult(int capacity,
                                   int maxWaiters,
                                   int maxNewWaiters,
                                   int maxWaitersLeaving,
                                   int maxPeopleGettingOffAtOnce,
                                   int maxPeopleGettingOnAtOnce,
                                   Instant timestamp) {
}
