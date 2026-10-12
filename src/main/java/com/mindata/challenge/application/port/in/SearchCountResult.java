package com.mindata.challenge.application.port.in;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;

public record SearchCountResult(
        String searchId,
        HotelAvailabilitySearch search,
        long count
) {
}