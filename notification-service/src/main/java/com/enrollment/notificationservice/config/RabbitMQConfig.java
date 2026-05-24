package com.enrollment.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ============================================================
// RABBITMQ CONFIGURATION - Exchanges, Queues, and Bindings
// ============================================================
// This sets up the RabbitMQ topology:
//
// TOPOLOGY:
//   notification.exchange (Topic Exchange)
//       │
//       ├── routing key: "email.send" → email.queue
//       │                                  │
//       │                                  └── Dead Letter → email.dlq
//       │
//       └── (future: "sms.send" → sms.queue)
//
// KEY RABBITMQ CONCEPTS:
//   - Exchange: Routes messages to queues based on routing keys
//   - Queue: Stores messages until a consumer picks them up
//   - Binding: Links an exchange to a queue with a routing key
//   - Dead Letter Queue (DLQ): Failed messages go here for inspection
//   - Topic Exchange: Routes based on pattern matching (*.send)
//
// WHY DLQ?
//   If email-worker fails to process a message after retries,
//   the message goes to email.dlq instead of being lost.
//   This is critical for production reliability.
// ============================================================

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "notification.exchange";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_DLQ = "email.dlq";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    // Dead Letter Exchange for failed messages
    public static final String DLX_EXCHANGE = "notification.dlx";

    // ==================== EXCHANGES ====================

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    // ==================== QUEUES ====================

    @Bean
    public Queue emailQueue() {
        // Configure email.queue with dead letter exchange
        // If a message fails, it's routed to email.dlq
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EMAIL_DLQ)
                .build();
    }

    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    // ==================== BINDINGS ====================

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(emailQueue)
                .to(notificationExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(Queue emailDeadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(emailDeadLetterQueue)
                .to(deadLetterExchange)
                .with(EMAIL_DLQ);
    }

    // ==================== MESSAGE CONVERTER ====================

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Use Jackson to serialize/deserialize messages as JSON
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
