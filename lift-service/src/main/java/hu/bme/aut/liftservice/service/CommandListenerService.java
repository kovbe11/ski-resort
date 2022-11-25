package hu.bme.aut.liftservice.service;

import hu.bme.aut.liftservice.service.dto.NewLiftStateMessage;
import hu.bme.aut.liftservice.service.dto.StateChangeAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommandListenerService {

    private final StateGeneratorService stateGeneratorService;
    private final StateChangeAckReporterService stateChangeAckReporterService;
    private final String liftId;

    public CommandListenerService(StateGeneratorService stateGeneratorService,
                                  StateChangeAckReporterService stateChangeAckReporterService,
                                  @Value("${lift-service.messaging.lift-id}") String liftId) {
        this.stateGeneratorService = stateGeneratorService;
        this.stateChangeAckReporterService = stateChangeAckReporterService;
        this.liftId = liftId;
    }

    @RabbitListener(queues = "#{commandQueue.name}")
    public void onCommand(NewLiftStateMessage newLiftStateMessage) {
        log.info("Change state command received: {}", newLiftStateMessage);
        stateGeneratorService.modifyLiftState(newLiftStateMessage);
        stateChangeAckReporterService.reportStateChange(new StateChangeAckMessage(liftId, stateGeneratorService.getCurrentLiftState()));
    }

}
