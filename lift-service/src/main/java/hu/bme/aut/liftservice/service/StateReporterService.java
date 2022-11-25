package hu.bme.aut.liftservice.service;

import hu.bme.aut.liftservice.service.dto.LiftStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StateReporterService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange stateExchange;
    private final String id;
    private final String stateRoutingPrefix;

    public StateReporterService(RabbitTemplate rabbitTemplate,
                                TopicExchange stateExchange,
                                @Value("${lift-service.messaging.state-routing-prefix}") String stateRoutingPrefix,
                                @Value("${lift-service.messaging.lift-id}") String id) {
        this.rabbitTemplate = rabbitTemplate;
        this.stateExchange = stateExchange;
        this.stateRoutingPrefix = stateRoutingPrefix;
        this.id = id;
    }

    public void reportState(LiftStateMessage liftStateMessage) {
        rabbitTemplate.convertAndSend(stateExchange.getName(), stateRoutingPrefix + id, liftStateMessage);
        log.info("State report: {}", liftStateMessage);
    }
}
