package com.mindata.challenge.infrastructure.adapter.in.rest.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;

public record SearchRequest(

    @Schema(description = "Hotel identifier", example = "1234aBc", requiredMode = Schema.RequiredMode.REQUIRED)
        String hotelId,

    @Schema(description = "Check-in date, format dd/MM/yyyy", example = "29/12/2026", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

    @Schema(description = "Check-out date, format dd/MM/yyyy; must be after checkIn", example = "31/12/2026", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

    @Schema(description = "Guest ages in order; each age must be zero or greater", example = "[30, 29, 1, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
        List<Integer> ages

) {

    public HotelAvailabilitySearch toDomain() {
        return new HotelAvailabilitySearch(
                hotelId,
                checkIn,
                checkOut,
                ages
        );
    }
}