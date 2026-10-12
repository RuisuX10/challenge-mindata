package com.mindata.challenge.domain.port.out;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;

import java.util.Optional;

public interface SearchRepositoryPort {

    void save(SearchCreatedEvent event);

    long count(HotelAvailabilitySearch search);

    Optional<SearchCreatedEvent> findById(String searchId);
}