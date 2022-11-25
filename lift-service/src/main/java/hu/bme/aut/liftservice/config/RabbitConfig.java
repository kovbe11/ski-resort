package hu.bme.aut.liftservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "-exchange";
    private final String stateQueueName;
    private final String connectionQueueName;
    private final String disconnectionQueueName;
    private final String stateChangeAckQueueName;
    private final String commandRoutingPrefix;
    private final String liftId;
    private final String commandExchangeName;

    public RabbitConfig(@Value("${lift-service.messaging.state-queue-name}") String stateQueueName,
                        @Value("${lift-service.messaging.connection-queue-name}") String connectionQueueName,
                        @Value("${lift-service.messaging.disconnection-queue-name}") String disconnectionQueueName,
                        @Value("${lift-service.messaging.state-change-ack-queue-name}") String stateChangeAckQueueName,
                        @Value("${lift-service.messaging.command-routing-prefix}") String commandRoutingPrefix,
                        @Value("${lift-service.messaging.lift-id}") String liftId,
                        @Value("${lift-service.messaging.command-exchange-name}") String commandExchangeName) {
        this.stateQueueName = stateQueueName;
        this.connectionQueueName = connectionQueueName;
        this.disconnectionQueueName = disconnectionQueueName;
        this.stateChangeAckQueueName = stateChangeAckQueueName;
        this.commandRoutingPrefix = commandRoutingPrefix;
        this.liftId = liftId;
        this.commandExchangeName = commandExchangeName;
    }

    @Bean
    public Queue commandQueue() {
        return new AnonymousQueue(new LiftIdNamingStrategy(liftId), Map.of("x-message-ttl", 5000));
    }

    @Bean
    DirectExchange commandExchange() {
        return new DirectExchange(commandExchangeName);
    }

    @Bean
    TopicExchange stateExchange() {
        return new TopicExchange(stateQueueName + EXCHANGE);
    }

    @Bean
    TopicExchange connectionExchange() {
        return new TopicExchange(connectionQueueName + EXCHANGE);
    }

    @Bean
    TopicExchange disconnectionExchange() {
        return new TopicExchange(disconnectionQueueName + EXCHANGE);
    }

    @Bean
    TopicExchange stateChangeAckExchange() {
        return new TopicExchange(stateChangeAckQueueName + EXCHANGE);
    }

    @Bean
    Binding commandBinding(Queue commandQueue, DirectExchange commandExchange) {
        return BindingBuilder.bind(commandQueue).to(commandExchange).with(commandRoutingPrefix + liftId);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

}
