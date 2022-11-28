package hu.bme.aut.service;

import hu.bme.aut.exception.SendingCommandIsUnsuccessfulException;
import hu.bme.aut.service.dto.LiftStateMessage;
import hu.bme.aut.service.dto.NewLiftStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static hu.bme.aut.exception.SendingCommandIsUnsuccessfulException.TIMEOUT;

@Service
@Slf4j
public class LiftControllingService {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange commandExchange;
    private final LiftStateControlSynchronizerService synchronizerService;
    private final String commandPrefix;

    public LiftControllingService(RabbitTemplate rabbitTemplate,
                                  DirectExchange commandExchange,
                                  LiftStateControlSynchronizerService synchronizerService, @Value("${controller-service.messaging.command-routing-prefix}") String commandPrefix) {
        this.rabbitTemplate = rabbitTemplate;
        this.commandExchange = commandExchange;
        this.synchronizerService = synchronizerService;
        this.commandPrefix = commandPrefix;
    }

    public Instant sendCommand(String liftId, LiftStateMessage.LiftState newState) {
        log.info("{} lift's state is changed to {}", liftId, newState);
        final var future = synchronizerService.setupSendingControl(liftId, newState);
        rabbitTemplate.convertAndSend(commandExchange.getName(), commandPrefix + liftId, new NewLiftStateMessage(newState));
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new SendingCommandIsUnsuccessfulException(liftId, newState, TIMEOUT);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e); //TODO better exception?
        }
    }

}
