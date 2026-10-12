package com.mindata.challenge.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import com.mindata.challenge.application.port.in.SearchCountResult;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Stored search and the number of matching searches")
public record SearchCountResponse(
        @Schema(description = "ID returned when the search was created")
        String searchId,
        @Schema(description = "Search criteria associated with the ID")
        SearchData search,
        @Schema(description = "Number of searches with identical criteria")
        long count
) {

    @Schema(description = "Search criteria")
    public record SearchData(
            @Schema(example = "1234aBc")
            String hotelId,

            @Schema(description = "Check-in date, format dd/MM/yyyy", example = "29/12/2026")
            @JsonFormat(pattern = "dd/MM/yyyy")
            LocalDate checkIn,

            @Schema(description = "Check-out date, format dd/MM/yyyy", example = "31/12/2026")
            @JsonFormat(pattern = "dd/MM/yyyy")
            LocalDate checkOut,

            @Schema(description = "Guest ages, preserving the submitted order", example = "[30, 29, 1, 3]")
            List<Integer> ages
    ) {
    }

    public static SearchCountResponse fromResult(SearchCountResult result) {

        HotelAvailabilitySearch search = result.search();

        SearchData searchData = new SearchData(
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        return new SearchCountResponse(
                result.searchId(),
                searchData,
                result.count()
        );
    }
}