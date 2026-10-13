package com.mindata.challenge.infrastructure.adapter.out.messaging;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.infrastructure.adapter.out.messaging.dto.SearchKafkaMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaSearchPublisherAdapterTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaSearchPublisherAdapter publisherAdapter;

    @Test 
    void shouldPublishSearchEventToKafka() throws Exception {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        SearchCreatedEvent event = new SearchCreatedEvent(
                "search-123",
                new HotelAvailabilitySearch(
                        "HOTEL123",
                        checkIn,
                        checkOut,
                        List.of(20, 30, 5)
                )
        );
        String serializedMessage = "{\"searchId\":\"search-123\"}";

        when(objectMapper.writeValueAsString(any(SearchKafkaMessage.class)))
                .thenReturn(serializedMessage);
        when(kafkaTemplate.send(
                "hotel_availability_searches",
                "search-123",
                serializedMessage
        )).thenReturn(CompletableFuture.completedFuture(null));

        publisherAdapter.publish(event);

        ArgumentCaptor<SearchKafkaMessage> messageCaptor =
                ArgumentCaptor.forClass(SearchKafkaMessage.class);
        verify(objectMapper).writeValueAsString(messageCaptor.capture());
        SearchKafkaMessage message = messageCaptor.getValue();

        assertAll(
                () -> assertEquals("search-123", message.searchId()),
                () -> assertEquals("HOTEL123", message.search().hotelId()),
                () -> assertEquals(checkIn, message.search().checkIn()),
                () -> assertEquals(checkOut, message.search().checkOut()),
                () -> assertEquals(List.of(20, 30, 5), message.search().ages())
        );
        verify(kafkaTemplate).send(
                "hotel_availability_searches",
                "search-123",
                serializedMessage
        );
    }
}