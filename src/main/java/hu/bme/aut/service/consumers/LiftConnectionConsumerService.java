package hu.bme.aut.service.consumers;

import hu.bme.aut.service.RegisteredLiftService;
import hu.bme.aut.service.dto.LiftConnectionMessage;
import hu.bme.aut.service.dto.LiftDisconnectionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class LiftConnectionConsumerService {

    private final RegisteredLiftService registeredLiftService;

    @RabbitListener(queues = "${controller-service.messaging.connection-queue-name}")
    public void onConnect(LiftConnectionMessage liftConnectionMessage) {
        log.info("Connected with: {}", liftConnectionMessage);
        registeredLiftService.liftConnected(liftConnectionMessage);
    }

    @RabbitListener(queues = "${controller-service.messaging.disconnection-queue-name}")
    public void onDisconnect(LiftDisconnectionMessage liftDisconnectionMessage) {
        log.info("Lift disconnected: {}", liftDisconnectionMessage);
        registeredLiftService.liftDisconnected(liftDisconnectionMessage.liftId());
    }

}
