package com.mindata.challenge.infrastructure.adapter.in.rest;

import com.mindata.challenge.application.port.in.CountSearchUseCase;
import com.mindata.challenge.application.port.in.SearchCountResult;
import com.mindata.challenge.application.port.in.SearchUseCase;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.infrastructure.adapter.in.rest.dto.SearchCountResponse;
import com.mindata.challenge.infrastructure.adapter.in.rest.dto.SearchRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock
    private SearchUseCase searchUseCase;

    @Mock
    private CountSearchUseCase countSearchUseCase;

    @InjectMocks
    private SearchController controller;

    @Test
    void shouldCreateSearch() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn.plusDays(5);

        SearchRequest request = new SearchRequest(
                "HOTEL123",
                checkIn,
                checkOut,
                List.of(20, 30, 5)
        );

        when(searchUseCase.createSearch(any(HotelAvailabilitySearch.class)))
                .thenReturn("search-123");

        var response = controller.createSearch(request);

        assertAll(
                () -> assertEquals(202, response.getStatusCode().value()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("search-123", response.getBody().get("searchId"))
        );

        verify(searchUseCase).createSearch(
                new HotelAvailabilitySearch(
                        "HOTEL123",
                        checkIn,
                        checkOut,
                        List.of(20, 30, 5)
                )
        );
    }

    @Test
    void shouldReturnSearchCount() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn.plusDays(5);

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                List.of(20, 30, 5)
        );

        SearchCountResult result = new SearchCountResult(
                "search-123",
                search,
                3L
        );

        when(countSearchUseCase.countSearches("search-123"))
                .thenReturn(result);

        var response = controller.countSearches("search-123");

        SearchCountResponse body = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals("search-123", body.searchId()),
                () -> assertEquals(3L, body.count()),
                () -> assertEquals("HOTEL123", body.search().hotelId()),
                () -> assertEquals(List.of(20, 30, 5), body.search().ages())
        );

        verify(countSearchUseCase).countSearches("search-123");
    }
}