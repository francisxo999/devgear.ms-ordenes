package com.devgear.ms_ordenes.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_ORDEN_CREADA = "orden.creada.exchange";

    @Bean
    public FanoutExchange ordenCreadaExchange() {
        return new FanoutExchange(EXCHANGE_ORDEN_CREADA);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}