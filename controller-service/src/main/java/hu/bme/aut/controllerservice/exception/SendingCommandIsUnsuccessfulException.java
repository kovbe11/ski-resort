package hu.bme.aut.controllerservice.exception;

import hu.bme.aut.controllerservice.service.dto.LiftStateMessage;
import lombok.Getter;

@Getter
public class SendingCommandIsUnsuccessfulException extends RuntimeException {
    public static final String CONCURRENT_REQUEST = "Concurrent control requests were detected.";
    public static final String TIMEOUT = "Request timed out!";

    public SendingCommandIsUnsuccessfulException(String liftId, LiftStateMessage.LiftState state, String reason) {
        super("Sending " + state + " command to lift with id " + liftId + " is unsuccessful. Reason is: " + reason);
    }

}
