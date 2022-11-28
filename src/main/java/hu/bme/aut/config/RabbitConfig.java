package hu.bme.aut.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    private static final String EXCHANGE = "-exchange";
    private final String stateQueueName;
    private final String stateChangeAckQueueName;
    private final String connectionQueueName;
    private final String disconnectionQueueName;
    private final String stateRoutingPrefix;
    private final String connectionRoutingPrefix;
    private final String commandExchangeName;
    private final String stateChangeAckPrefix;

    public RabbitConfig(@Value("${controller-service.messaging.command-exchange-name}") String commandExchangeName,
                        @Value("${controller-service.messaging.state-queue-name}") String stateQueueName,
                        @Value("${controller-service.messaging.connection-queue-name}") String connectionQueueName,
                        @Value("${controller-service.messaging.state-change-ack-queue-name}") String stateChangeAckQueueName,
                        @Value("${controller-service.messaging.disconnection-queue-name}") String disconnectionQueueName,
                        @Value("${controller-service.messaging.state-change-ack-prefix}") String stateChangeAckPrefix,
                        @Value("${controller-service.messaging.state-routing-prefix}") String stateRoutingPrefix,
                        @Value("${controller-service.messaging.connection-routing-prefix}") String connectionRoutingPrefix
    ) {
        this.stateQueueName = stateQueueName;
        this.stateRoutingPrefix = stateRoutingPrefix;
        this.connectionQueueName = connectionQueueName;
        this.disconnectionQueueName = disconnectionQueueName;
        this.connectionRoutingPrefix = connectionRoutingPrefix;
        this.commandExchangeName = commandExchangeName;
        this.stateChangeAckQueueName = stateChangeAckQueueName;
        this.stateChangeAckPrefix = stateChangeAckPrefix;
    }

    @Bean
    public Queue stateQueue() {
        return new Queue(stateQueueName, false, false, false, Map.of("x-message-ttl", 5000));
    }

    @Bean
    public Queue connectionQueue() {
        return new Queue(connectionQueueName, false, false, false, Map.of("x-message-ttl", 5000));
    }

    @Bean
    public Queue disconnectionQueue() {
        return new Queue(disconnectionQueueName, false, false, false, Map.of("x-message-ttl", 5000));
    }

    @Bean
    Queue stateChangeAckQueue() {
        return new Queue(stateChangeAckQueueName, false, false, false, Map.of("x-message-ttl", 5000));
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
    Binding stateBinding(Queue stateQueue, TopicExchange stateExchange) {
        return BindingBuilder.bind(stateQueue).to(stateExchange).with(stateRoutingPrefix + "*");
    }

    @Bean
    Binding connectionBinding(Queue connectionQueue, TopicExchange connectionExchange) {
        return BindingBuilder.bind(connectionQueue).to(connectionExchange).with(connectionRoutingPrefix + "*");
    }

    @Bean
    Binding disconnectionBinding(Queue disconnectionQueue, TopicExchange disconnectionExchange) {
        return BindingBuilder.bind(disconnectionQueue).to(disconnectionExchange).with(connectionRoutingPrefix + "*");
    }

    @Bean
    Binding stateControlAckBinding(Queue stateChangeAckQueue, TopicExchange stateChangeAckExchange) {
        return BindingBuilder.bind(stateChangeAckQueue).to(stateChangeAckExchange).with(stateChangeAckPrefix + "*");
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
