package hu.bme.aut.liftservice.service;


import hu.bme.aut.liftservice.config.ServiceConfiguration;
import hu.bme.aut.liftservice.service.dto.LiftConnectionMessage;
import hu.bme.aut.liftservice.service.dto.LiftDisconnectionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
@Slf4j
public class ConnectionReporterService {

    private final RabbitTemplate rabbitTemplate;
    private final String liftId;
    private final ServiceConfiguration serviceConfiguration;
    private final TopicExchange connectionExchange;
    private final String connectionRoutePrefix;
    private final TopicExchange disconnectionExchange;

    public ConnectionReporterService(RabbitTemplate rabbitTemplate,
                                     TopicExchange connectionExchange,
                                     TopicExchange disconnectionExchange,
                                     ServiceConfiguration serviceConfiguration,
                                     @Value("${lift-service.messaging.lift-id}") String liftId,
                                     @Value("${lift-service.messaging.connection-routing-prefix}") String connectionRoutePrefix) {
        this.rabbitTemplate = rabbitTemplate;
        this.connectionExchange = connectionExchange;
        this.disconnectionExchange = disconnectionExchange;
        this.liftId = liftId;
        this.serviceConfiguration = serviceConfiguration;
        this.connectionRoutePrefix = connectionRoutePrefix;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connect() {
        rabbitTemplate.convertAndSend(connectionExchange.getName(), connectionRoutePrefix + liftId, createConnectionMessage());
    }

    @PreDestroy
    public void disconnect() {
        rabbitTemplate.convertAndSend(connectionExchange.getName(), connectionRoutePrefix + liftId, createDisconnectionMessage());
    }

    private LiftDisconnectionMessage createDisconnectionMessage() {
        return new LiftDisconnectionMessage(liftId);
    }

    private LiftConnectionMessage createConnectionMessage() {
        return new LiftConnectionMessage(
                liftId,
                serviceConfiguration.getCapacity(),
                serviceConfiguration.getMaxWaiters(),
                serviceConfiguration.getMaxNewWaiters(),
                serviceConfiguration.getMaxWaitersLeaving(),
                serviceConfiguration.getMaxPeopleGettingOffAtOnce(),
                serviceConfiguration.getMaxPeopleGettingOnAtOnce());
    }


}
