package hu.bme.aut.service;

import hu.bme.aut.exception.SendingCommandIsUnsuccessfulException;
import hu.bme.aut.service.dto.LiftStateMessage.LiftState;
import hu.bme.aut.service.dto.StateChangeAckMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static hu.bme.aut.exception.SendingCommandIsUnsuccessfulException.CONCURRENT_REQUEST;

@Service
public class LiftStateControlSynchronizerService {

    private final HashMap<String, CompletableFuture<Instant>> currentlyPendingControlRequests;

    public LiftStateControlSynchronizerService(HashMap<String, CompletableFuture<Instant>> currentlyPendingControlRequests) {
        this.currentlyPendingControlRequests = currentlyPendingControlRequests;
    }

    public CompletableFuture<Instant> setupSendingControl(String liftId, LiftState liftState) {
        final var future = new CompletableFuture<Instant>();
        synchronized (currentlyPendingControlRequests) {
            if (currentlyPendingControlRequests.containsKey(liftId)) {
                throw new SendingCommandIsUnsuccessfulException(liftId, liftState, CONCURRENT_REQUEST);
            }
            currentlyPendingControlRequests.put(liftId, future);
        }
        return future;
    }

    public void completeAcknowledgement(StateChangeAckMessage message) {
        synchronized (currentlyPendingControlRequests) {
            Optional.ofNullable(currentlyPendingControlRequests.get(message.liftId()))
                    .ifPresent(future -> future.complete(message.timestamp()));
        }
    }
}
