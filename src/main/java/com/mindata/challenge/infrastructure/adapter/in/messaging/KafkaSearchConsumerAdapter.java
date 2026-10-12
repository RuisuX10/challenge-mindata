package com.mindata.challenge.infrastructure.adapter.in.messaging;

import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;
import com.mindata.challenge.infrastructure.adapter.out.messaging.dto.SearchKafkaMessage;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaSearchConsumerAdapter {

    private final SearchRepositoryPort repositoryPort;
    private final ObjectMapper objectMapper;

    public KafkaSearchConsumerAdapter(
            SearchRepositoryPort repositoryPort,
            ObjectMapper objectMapper
    ) {
        this.repositoryPort = repositoryPort;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "hotel_availability_searches",
            groupId = "challenge-search-consumer"
    )
    public void consume(String json) {

        SearchKafkaMessage message = objectMapper.readValue(
                json,
                SearchKafkaMessage.class
        );

        SearchCreatedEvent event = message.toDomain();

        repositoryPort.save(event);
    }
}