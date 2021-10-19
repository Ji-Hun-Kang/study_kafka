package com.jikang.rabbitmq.receiver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConfig {
    private static final String RABBITMQ_HOST = "localhost";
    private static final int RABBITMQ_PORT = 5672;

    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";

    private static final String ROUTING_KEY = "test-topic-1.*";
    private static final String EXCHANGE_NAME = "test-exchange-1";
    private static final String QUEUE_NAME = "test-queue-1";

    /**
     * RabbitMQ Server와의 Connection을 생성하는 Factory 객체를 반환한다.
     *
     * @return 초기화된 RabbitMQ ConnectionFactory 객체
     */
    @Bean
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST, RABBITMQ_PORT);
        ((CachingConnectionFactory) connectionFactory).setUsername(USERNAME);
        ((CachingConnectionFactory) connectionFactory).setPassword(PASSWORD);
        return connectionFactory;
    }

    /**
     * RabbitMQ에서 사용할 Exchange를 선언하고 반환한다.
     * Exchange 종류는 Topic 사용
     *
     * @return 초기화된 TopicExchange 객체
     */
    @Bean
    public TopicExchange getExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * RabbitMQ에서 사용할 Queue를 선언하고 반환한다.
     *
     * @return 초기화된 Queue 객체
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Exchange와 Queue를 연결하는 Binding 객체를 선언하고 반환한다.
     *
     * @param queue    초기화된 Queue
     * @param exchange 초기화된 TopicExchange
     * @return Binding 객체
     */
    @Bean
    public Binding getBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * RabbitMQ에서 메시지를 수신할 Listener의 Factory를 선언하고 반환한다.
     *
     * @param connectionFactory ConnectionFactory 객체
     * @param converter         Jackson Converter 객체. byte[] <-> 메시지 간 변환을 담당
     * @return 초기화된 Listener Factory 객체
     */
    @Bean("SampleContainerFactory")
    SimpleRabbitListenerContainerFactory getSampleContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}