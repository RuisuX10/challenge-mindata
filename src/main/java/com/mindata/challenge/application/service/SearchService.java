package com.mindata.challenge.application.service;

import java.util.UUID;

import com.mindata.challenge.application.port.in.SearchUseCase;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchEventPublisherPort;

public class SearchService implements SearchUseCase {

    private final SearchEventPublisherPort publisherPort;

    public SearchService(SearchEventPublisherPort publisherPort) {
        this.publisherPort = publisherPort;
    }

    @Override
    public String createSearch(HotelAvailabilitySearch search) {

        String searchId = UUID.randomUUID().toString();

        SearchCreatedEvent event = new SearchCreatedEvent(
            searchId,
            search
        );

        publisherPort.publish(event);

        return searchId;
    }
}