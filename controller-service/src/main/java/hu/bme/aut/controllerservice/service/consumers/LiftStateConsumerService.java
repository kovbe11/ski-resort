package hu.bme.aut.controllerservice.service.consumers;

import hu.bme.aut.controllerservice.service.RegisteredLiftService;
import hu.bme.aut.controllerservice.service.dto.LiftStateMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class LiftStateConsumerService {

    private final RegisteredLiftService registeredLiftService;

    @RabbitListener(queues = "${controller-service.messaging.state-queue-name}")
    public void onStateReceived(LiftStateMessage liftState) {
        log.info("State received: {}", liftState);
        registeredLiftService.newStateReceived(liftState);
    }

}
