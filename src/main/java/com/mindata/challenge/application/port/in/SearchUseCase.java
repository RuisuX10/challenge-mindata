package com.mindata.challenge.application.port.in;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;

public interface SearchUseCase {

    String createSearch(HotelAvailabilitySearch search);

}