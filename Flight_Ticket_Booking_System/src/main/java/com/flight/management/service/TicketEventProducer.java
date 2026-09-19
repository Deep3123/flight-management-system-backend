package com.flight.management.service;

import com.flight.management.configuration.RabbitMQConfig;
import com.flight.management.proxy.TicketProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketEventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishTicketEvent(TicketProxy ticketProxy) {
        log.info("Publishing ticket event to RabbitMQ queue: {} for payment ID: {}", 
                RabbitMQConfig.TICKET_QUEUE, ticketProxy.getPaymentId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_QUEUE, ticketProxy);
        log.info("Ticket event published successfully for payment ID: {}", ticketProxy.getPaymentId());
    }
}
