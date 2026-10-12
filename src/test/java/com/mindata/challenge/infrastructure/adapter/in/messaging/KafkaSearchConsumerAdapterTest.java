package com.mindata.challenge.infrastructure.adapter.in.messaging;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;
import com.mindata.challenge.infrastructure.adapter.out.messaging.dto.SearchKafkaMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaSearchConsumerAdapterTest {

    @Mock
    private SearchRepositoryPort repositoryPort;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaSearchConsumerAdapter consumerAdapter;

    @Test
    void shouldConsumeMessageAndSaveSearch() {

        String json = """
                {
                  "searchId": "search-123",
                  "search": {
                    "hotelId": "HOTEL123",
                    "checkIn": "15/10/2026",
                    "checkOut": "20/10/2026",
                    "ages": [20, 30, 5]
                  }
                }
                """;

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                "HOTEL123",
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20),
                List.of(20, 30, 5)
        );

        SearchCreatedEvent event = new SearchCreatedEvent(
                "search-123",
                search
        );

        SearchKafkaMessage message = SearchKafkaMessage.fromDomain(event);

        when(objectMapper.readValue(json, SearchKafkaMessage.class))
                .thenReturn(message);

        consumerAdapter.consume(json);

        verify(objectMapper).readValue(json, SearchKafkaMessage.class);

        verify(repositoryPort, times(1)).save(event);
    }
}