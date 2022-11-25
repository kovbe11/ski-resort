package hu.bme.aut.liftservice.service.dto;

import java.time.Instant;

public record LiftConnectionMessage(String liftId,
                                    int capacity,
                                    int maxWaiters,
                                    int maxNewWaiters,
                                    int maxWaitersLeaving,
                                    int maxPeopleGettingOffAtOnce,
                                    int maxPeopleGettingOnAtOnce,
                                    Instant timestamp) {

    public LiftConnectionMessage(String liftId,
                                 int capacity,
                                 int maxWaiters,
                                 int maxNewWaiters,
                                 int maxWaitersLeaving,
                                 int maxPeopleGettingOffAtOnce,
                                 int maxPeopleGettingOnAtOnce) {
        this(liftId,
                capacity,
                maxWaiters,
                maxNewWaiters,
                maxWaitersLeaving,
                maxPeopleGettingOffAtOnce,
                maxPeopleGettingOnAtOnce,
                Instant.now());
    }

}
