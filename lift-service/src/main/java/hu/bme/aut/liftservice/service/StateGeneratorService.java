package hu.bme.aut.liftservice.service;


import hu.bme.aut.liftservice.config.ServiceConfiguration;
import hu.bme.aut.liftservice.service.dto.LiftStateMessage;
import hu.bme.aut.liftservice.service.dto.NewLiftStateMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static hu.bme.aut.liftservice.service.StateGeneratorService.LiftState.CLOSED;
import static hu.bme.aut.liftservice.service.StateGeneratorService.LiftState.OPEN;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

@Service
@Slf4j
public class StateGeneratorService {
    private final RandomService randomService;
    private final StateReporterService stateReporterService;

    private final int capacity;
    private final int maxWaiters;
    private final int maxNewWaiters;
    private final int maxPeopleGettingOffAtOnce;
    private final int maxPeopleGettingOnAtOnce;
    private final int maxWaitersLeaving;
    private final String liftId;

    private int currentlyOnLift = 0;
    private int currentlyWaiting;
    @Getter
    private LiftState currentLiftState = OPEN;

    public StateGeneratorService(@Value("${lift-service.messaging.lift-id}") String liftId,
                                 ServiceConfiguration serviceConfiguration,
                                 RandomService randomService,
                                 StateReporterService stateReporterService) {
        this.liftId = liftId;
        this.capacity = serviceConfiguration.getCapacity();
        this.maxNewWaiters = serviceConfiguration.getMaxNewWaiters();
        this.randomService = randomService;
        this.currentlyWaiting = serviceConfiguration.getCurrentlyWaiting();
        this.maxWaiters = serviceConfiguration.getMaxWaiters();
        this.maxWaitersLeaving = serviceConfiguration.getMaxWaitersLeaving();
        this.maxPeopleGettingOffAtOnce = serviceConfiguration.getMaxPeopleGettingOffAtOnce();
        this.maxPeopleGettingOnAtOnce = serviceConfiguration.getMaxPeopleGettingOnAtOnce();
        this.stateReporterService = stateReporterService;
        log.info("Capacity: {}", capacity);
        log.info("Max waiters: {}, Max new waiters: {}", maxWaiters, maxNewWaiters);
        log.info("Currently waiting: {}", currentlyWaiting);
        log.info("Max people getting off: {}, Max people getting on: {}", maxPeopleGettingOffAtOnce, maxPeopleGettingOnAtOnce);
    }

    @Scheduled(initialDelay = 2100, fixedRateString = "${lift-service.schedules.update-state-in-millis}")
    public void updateState() {
        currentlyOnLift = max(0, currentlyOnLift - maxPeopleGettingOffAtOnce);
        if (currentLiftState != OPEN) {
            reportState();
            return;
        }

        final int peopleThatCanGetOn = min(maxPeopleGettingOnAtOnce, currentlyWaiting);
        if (capacity < peopleThatCanGetOn + currentlyOnLift) {
            fillToCapacity();
        } else {
            fillWith(peopleThatCanGetOn);
        }
        reportState();
    }

    @Scheduled(initialDelay = 2000, fixedRateString = "${lift-service.schedules.add-waiters-in-millis}")
    public void addWaitingPeople() {
        if (currentLiftState == CLOSED) {
            currentlyWaiting = max(0, currentlyWaiting - (randomService.getNextInt(maxWaitersLeaving) + 1));
        } else {
            currentlyWaiting = min(currentlyWaiting + randomService.getNextInt(maxNewWaiters + 1), maxWaiters);
        }
        reportState();
    }

    public void modifyLiftState(NewLiftStateMessage message) {
        String logMessage;
        switch (message.liftState()) {
            case OPEN -> logMessage = "Lift opened";
            case CLOSED -> logMessage = "Lift closed";
            case TEMPORARILY_CLOSED -> logMessage = "Lift temporarily closed";
            default -> throw new RuntimeException("impossible");
        }
        currentLiftState = message.liftState();
        log.info(logMessage);
    }

    private void reportState() {
        final var liftStateMessage = new LiftStateMessage(liftId, currentlyOnLift, currentlyWaiting, currentLiftState);
        log.info("Update: {}", liftStateMessage);
        stateReporterService.reportState(liftStateMessage);
    }

    private void fillWith(int peopleThatCanGetOn) {
        currentlyOnLift += peopleThatCanGetOn;
        currentlyWaiting -= peopleThatCanGetOn;
    }

    private void fillToCapacity() {
        final int peopleGettingOn = capacity - currentlyOnLift;
        currentlyWaiting -= peopleGettingOn;
        currentlyOnLift = capacity;
    }

    public enum LiftState {
        OPEN, CLOSED, TEMPORARILY_CLOSED
    }

}
