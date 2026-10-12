package com.mindata.challenge.application.service;

import com.mindata.challenge.application.port.in.SearchCountResult;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountSearchServiceTest {

    @Mock
    private SearchRepositoryPort repositoryPort;

    @InjectMocks
    private CountSearchService service;

    @Test
    void shouldCountMatchingSearches() {

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

        when(repositoryPort.findById("search-123"))
                .thenReturn(Optional.of(event));

        when(repositoryPort.count(search))
                .thenReturn(3L);

        SearchCountResult result = service.countSearches("search-123");

        assertAll(
                () -> assertEquals("search-123", result.searchId()),
                () -> assertEquals(search, result.search()),
                () -> assertEquals(3L, result.count())
        );

        verify(repositoryPort).findById("search-123");
        verify(repositoryPort).count(search);
    }

    @Test
    void shouldThrowExceptionWhenSearchDoesNotExist() {

        when(repositoryPort.findById("invalid-id"))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> service.countSearches("invalid-id")
        );

        assertTrue(exception.getMessage().contains("invalid-id"));

        verify(repositoryPort, never()).count(any());
    }
}