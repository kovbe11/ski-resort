package hu.bme.aut.liftservice.service;

import hu.bme.aut.liftservice.service.dto.StateChangeAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StateChangeAckReporterService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange stateChangeAckExchange;
    private final String id;
    private final String stateChangeAckPrefix;

    public StateChangeAckReporterService(RabbitTemplate rabbitTemplate,
                                         TopicExchange stateChangeAckExchange,
                                         @Value("${lift-service.messaging.state-change-ack-prefix}") String stateChangeAckPrefix,
                                         @Value("${lift-service.messaging.lift-id}") String id) {
        this.rabbitTemplate = rabbitTemplate;
        this.stateChangeAckExchange = stateChangeAckExchange;
        this.id = id;
        this.stateChangeAckPrefix = stateChangeAckPrefix;
    }

    public void reportStateChange(StateChangeAckMessage stateChangeAckMessage) {
        rabbitTemplate.convertAndSend(stateChangeAckExchange.getName(), stateChangeAckPrefix + id, stateChangeAckMessage);
        log.info("State change acknowledged at: {}", stateChangeAckMessage);
    }


}
