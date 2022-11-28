package hu.bme.aut.service.consumers;

import hu.bme.aut.service.LiftStateControlSynchronizerService;
import hu.bme.aut.service.dto.StateChangeAckMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class LiftStateChangeAckConsumerService {

    private final LiftStateControlSynchronizerService synchronizerService;

    @RabbitListener(queues = "${controller-service.messaging.state-change-ack-queue-name}")
    public void onAckReceived(StateChangeAckMessage stateChangeAckMessage) {
        log.info("State change acknowledged with: {}", stateChangeAckMessage);
        synchronizerService.completeAcknowledgement(stateChangeAckMessage);
    }

}
