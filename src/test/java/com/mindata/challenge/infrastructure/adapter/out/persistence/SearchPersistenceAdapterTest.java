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

        persistenceAdapter.save(event);

        ArgumentCaptor<HotelSearchJpaEntity> captor =
                ArgumentCaptor.forClass(HotelSearchJpaEntity.class);

        verify(jpaRepository, times(1)).save(captor.capture());

        HotelSearchJpaEntity savedEntity = captor.getValue();

        assertAll(
                () -> assertEquals("search-123", savedEntity.getSearchId()),
                () -> assertEquals("HOTEL123", savedEntity.getHotelId()),
                () -> assertEquals(
                        LocalDate.of(2026, 10, 15),
                        savedEntity.getCheckIn()
                ),
                () -> assertEquals(
                        LocalDate.of(2026, 10, 20),
                        savedEntity.getCheckOut()
                ),
                () -> assertEquals("20,30,5", savedEntity.getAges())
        );
    }
}