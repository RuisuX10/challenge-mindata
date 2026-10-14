package com.mindata.challenge.infrastructure.adapter.out.persistence;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchPersistenceAdapterTest {

    @Mock
    private HotelSearchJpaRepository jpaRepository;

    @InjectMocks
    private SearchPersistenceAdapter persistenceAdapter;

    @Test
    void shouldSaveSearchCorrectly() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn.plusDays(5);

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                List.of(20, 30, 5)
        );

        SearchCreatedEvent event = new SearchCreatedEvent(
                "search-123",
                search
        );

        persistenceAdapter.save(event);

        ArgumentCaptor<HotelSearchJpaEntity> captor =
                ArgumentCaptor.forClass(HotelSearchJpaEntity.class);

        verify(jpaRepository, times(1)).save(captor.capture());

        HotelSearchJpaEntity savedEntity = captor.getValue();

        assertAll(
                () -> assertEquals("search-123", savedEntity.getSearchId()),
                () -> assertEquals("HOTEL123", savedEntity.getHotelId()),
                () -> assertEquals(checkIn, savedEntity.getCheckIn()),
                () -> assertEquals(checkOut, savedEntity.getCheckOut()),
                () -> assertEquals("20,30,5", savedEntity.getAges())
        );
    }

    @Test
    void shouldCountMatchingSearches() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                List.of(20, 30, 5)
        );

        when(jpaRepository.countMatchingSearches(
                "HOTEL123",
                checkIn,
                checkOut,
                "20,30,5"
        )).thenReturn(3L);

        long count = persistenceAdapter.count(search);

        assertEquals(3L, count);
        verify(jpaRepository).countMatchingSearches(
                "HOTEL123",
                checkIn,
                checkOut,
                "20,30,5"
        );
    }

    @Test
    void shouldMapFoundEntityToSearchEvent() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        HotelSearchJpaEntity entity = new HotelSearchJpaEntity(
                "search-123",
                "HOTEL123",
                checkIn,
                checkOut,
                "20,30,5"
        );
        SearchCreatedEvent expected = new SearchCreatedEvent(
                "search-123",
                new HotelAvailabilitySearch(
                        "HOTEL123",
                        checkIn,
                        checkOut,
                        List.of(20, 30, 5)
                )
        );
        when(jpaRepository.findById("search-123"))
                .thenReturn(Optional.of(entity));

        Optional<SearchCreatedEvent> result =
                persistenceAdapter.findById("search-123");

        assertEquals(Optional.of(expected), result);
    }

    @Test
    void shouldReturnEmptyWhenSearchIdDoesNotExist() {

        when(jpaRepository.findById("missing-id"))
                .thenReturn(Optional.empty());

        Optional<SearchCreatedEvent> result =
                persistenceAdapter.findById("missing-id");

        assertTrue(result.isEmpty());
    }
}