package hu.bme.aut.controllerservice.service;

import hu.bme.aut.controllerservice.service.dto.LiftConnectionMessage;
import hu.bme.aut.controllerservice.service.dto.LiftConnectionResult;
import hu.bme.aut.controllerservice.service.dto.LiftDataResult;
import hu.bme.aut.controllerservice.service.dto.LiftStateMessage;
import hu.bme.aut.controllerservice.service.dto.LiftStateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static hu.bme.aut.controllerservice.service.dto.LiftStateMessage.LiftState.JUST_CONNECTED;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class RegisteredLiftService {

    private final ConcurrentHashMap<String, LiftStateData> currentlyRegisteredLifts;

    public RegisteredLiftService(ConcurrentHashMap<String, LiftStateData> currentlyRegisteredLifts) {
        if (currentlyRegisteredLifts == null) {
            currentlyRegisteredLifts = new ConcurrentHashMap<>();
        }
        this.currentlyRegisteredLifts = currentlyRegisteredLifts;
    }

    public RegisteredLiftService() {
        this(new ConcurrentHashMap<>());
    }

    @Scheduled(fixedRate = 5, timeUnit = SECONDS)
    public /*List<LiftDataResult>*/ void currentlyRegisteredLifts() {
        final var result = currentlyRegisteredLifts.entrySet().stream()
                .map(it -> new LiftDataResult(it.getKey(),
                        mapToResult(it.getValue().liftConnectionMessage()),
                        mapToResult(it.getValue().liftStateMessage()))
                )
                .toList();
        log.info("Currently registered lifts: {}", result);
    }

    public void liftConnected(LiftConnectionMessage liftConnectionMessage) {
        currentlyRegisteredLifts.compute(liftConnectionMessage.liftId(), (key, oldValue) -> Optional.ofNullable(oldValue)
                .map(it -> it.connectionReceived(liftConnectionMessage)) // should not happen, improve by acknowledgements
                .orElseGet(() -> new LiftStateData(liftConnectionMessage)));
    }

    public void liftDisconnected(String liftId) {
        currentlyRegisteredLifts.remove(liftId);
    }

    public void newStateReceived(LiftStateMessage liftState) {
        currentlyRegisteredLifts.compute(liftState.liftId(), (key, oldValue) -> Optional.ofNullable(oldValue)
                .map(it -> it.stateChanged(liftState))
                .orElseGet(() -> new LiftStateData(liftState))); // should not happen, improve by acknowledgements
    }

    @Scheduled(fixedRateString = "${controller-service.schedules.reports-missed}")
    public void checkReportMissed() {
        currentlyRegisteredLifts.values().parallelStream()
                .flatMap(it ->
                        (it.liftStateMessage.timestamp().isBefore(Instant.now().minusSeconds(2))) ?
                                Stream.of(it.liftStateMessage.liftId()) : Stream.empty())
                .forEach(it -> {
                    final var liftState = currentlyRegisteredLifts.get(it);
                    if (liftState.missedReports >= 5) {
                        currentlyRegisteredLifts.remove(it);
                    } else {
                        currentlyRegisteredLifts.put(it, liftState.reportMissed());
                    }
                });
    }

    private LiftConnectionResult mapToResult(LiftConnectionMessage liftConnectionMessage) {
        return new LiftConnectionResult(liftConnectionMessage.capacity(),
                liftConnectionMessage.maxWaiters(),
                liftConnectionMessage.maxNewWaiters(),
                liftConnectionMessage.maxWaitersLeaving(),
                liftConnectionMessage.maxPeopleGettingOffAtOnce(),
                liftConnectionMessage.maxPeopleGettingOnAtOnce(),
                liftConnectionMessage.timestamp());
    }

    private LiftStateResult mapToResult(LiftStateMessage liftStateMessage) {
        return new LiftStateResult(liftStateMessage.currentlyOnLift(),
                liftStateMessage.currentlyWaitingForLift(),
                liftStateMessage.state(),
                liftStateMessage.timestamp());
    }

    record LiftStateData(LiftStateMessage liftStateMessage, LiftConnectionMessage liftConnectionMessage,
                         int missedReports) {
        LiftStateData(LiftConnectionMessage liftConnectionMessage) {
            this(new LiftStateMessage(liftConnectionMessage.liftId(), -1, -1, JUST_CONNECTED, Instant.now()), liftConnectionMessage, 0);
        }

        LiftStateData(LiftStateMessage liftStateMessage) {
            this(liftStateMessage, new LiftConnectionMessage(liftStateMessage.liftId(), -1, -1, -1, -1, -1, -1), 0);
        }

        public LiftStateData reportMissed() {
            return new LiftStateData(liftStateMessage, liftConnectionMessage, missedReports + 1);
        }

        public LiftStateData connectionReceived(LiftConnectionMessage liftConnectionMessage) {
            return new LiftStateData(liftStateMessage, liftConnectionMessage, missedReports);
        }

        public LiftStateData stateChanged(LiftStateMessage liftStateMessage) {
            return new LiftStateData(liftStateMessage, liftConnectionMessage, missedReports);
        }
    }
}
