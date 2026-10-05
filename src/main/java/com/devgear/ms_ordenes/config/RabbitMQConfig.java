package com.devgear.ms_ordenes.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombre del exchange compartido por todo el sistema: Productos, Notificaciones
    // y Despachos lo van a usar (en sus propios proyectos) para crear su cola y
    // engancharla acá. Debe escribirse IGUAL en los 4 microservicios.
    public static final String EXCHANGE_ORDEN_CREADA = "orden.creada.exchange";

    @Bean
    public FanoutExchange ordenCreadaExchange() {
        return new FanoutExchange(EXCHANGE_ORDEN_CREADA);
    }

    // Spring Boot detecta este bean automáticamente y lo usa para el RabbitTemplate
    // (y para los @RabbitListener que agreguemos más adelante en otros servicios).
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}