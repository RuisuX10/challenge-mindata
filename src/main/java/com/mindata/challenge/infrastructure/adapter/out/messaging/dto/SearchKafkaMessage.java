package com.mindata.challenge.infrastructure.adapter.out.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;

import java.time.LocalDate;
import java.util.List;

public record SearchKafkaMessage(
        String searchId,
        SearchData search
) {

    public record SearchData(
            String hotelId,

            @JsonFormat(pattern = "dd/MM/yyyy")
            LocalDate checkIn,

            @JsonFormat(pattern = "dd/MM/yyyy")
            LocalDate checkOut,

            List<Integer> ages
    ) {
    }

    public static SearchKafkaMessage fromDomain(SearchCreatedEvent event) {

        HotelAvailabilitySearch search = event.search();

        return new SearchKafkaMessage(
                event.searchId(),
                new SearchData(
                        search.hotelId(),
                        search.checkIn(),
                        search.checkOut(),
                        search.ages()
                )
        );
    }

    public SearchCreatedEvent toDomain() {

        HotelAvailabilitySearch domainSearch = new HotelAvailabilitySearch(
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        return new SearchCreatedEvent(searchId, domainSearch);
    }
}