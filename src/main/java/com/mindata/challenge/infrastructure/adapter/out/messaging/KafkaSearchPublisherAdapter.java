package com.mindata.challenge.infrastructure.adapter.out.messaging;

import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchEventPublisherPort;
import com.mindata.challenge.infrastructure.adapter.out.messaging.dto.SearchKafkaMessage;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaSearchPublisherAdapter implements SearchEventPublisherPort {

    private static final String TOPIC = "hotel_availability_searches";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaSearchPublisherAdapter(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(SearchCreatedEvent event) {

        SearchKafkaMessage message = SearchKafkaMessage.fromDomain(event);

        try {
            String json = objectMapper.writeValueAsString(message);

            kafkaTemplate.send(TOPIC, event.searchId(), json).join();

        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Error al serializar el evento de búsqueda", e
            );
        }
    }
}