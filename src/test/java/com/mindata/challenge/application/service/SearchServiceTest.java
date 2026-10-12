package com.mindata.challenge.application.service;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchEventPublisherPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchEventPublisherPort publisherPort;

    @InjectMocks
    private SearchService searchService;

    @Test
    void shouldCreateSearchAndPublishEvent() {

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
            "HOTEL123",
            LocalDate.of(2026, 10, 15),
            LocalDate.of(2026, 10, 20),
            List.of(25, 30)
        );

        String searchId = searchService.createSearch(search);

        ArgumentCaptor<SearchCreatedEvent> captor =
            ArgumentCaptor.forClass(SearchCreatedEvent.class);

        verify(publisherPort, times(1)).publish(captor.capture());

        SearchCreatedEvent publishedEvent = captor.getValue();

        assertAll(
            () -> assertNotNull(searchId),
            () -> assertDoesNotThrow(() -> UUID.fromString(searchId)),
            () -> assertEquals(searchId, publishedEvent.searchId()),
            () -> assertEquals(search, publishedEvent.search())
        );
    }
}