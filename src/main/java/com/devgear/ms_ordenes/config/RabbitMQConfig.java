package com.devgear.ms_ordenes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_ORDEN_CREADA = "orden.creada.exchange";

    // Estado de los despachos (lo publica ms-despachos). Aquí solo se consume.
    public static final String EXCHANGE_DESPACHO_ESTADO = "despacho.estado.exchange";
    public static final String QUEUE_DESPACHO_ESTADO = "ordenes.despacho-estado.queue";

    @Bean
    public TopicExchange despachoEstadoExchange() {
        return new TopicExchange(EXCHANGE_DESPACHO_ESTADO);
    }

    @Bean
    public Queue despachoEstadoQueue() {
        return new Queue(QUEUE_DESPACHO_ESTADO, true); // durable: si ms-ordenes está caído, el aviso espera
    }

    @Bean
    public Binding despachoEstadoBinding(Queue despachoEstadoQueue, TopicExchange despachoEstadoExchange) {
        return BindingBuilder.bind(despachoEstadoQueue).to(despachoEstadoExchange).with("despacho.*");
    }

    @Bean
    public FanoutExchange ordenCreadaExchange() {
        return new FanoutExchange(EXCHANGE_ORDEN_CREADA);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
