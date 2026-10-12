package com.mindata.challenge.application.service;

import com.mindata.challenge.application.port.in.CountSearchUseCase;
import com.mindata.challenge.application.port.in.SearchCountResult;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;

import java.util.NoSuchElementException;

public class CountSearchService implements CountSearchUseCase {

    private final SearchRepositoryPort repositoryPort;

    public CountSearchService(SearchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SearchCountResult countSearches(String searchId) {

        SearchCreatedEvent event = repositoryPort.findById(searchId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe una búsqueda con el searchId: " + searchId
                ));

        long count = repositoryPort.count(event.search());

        return new SearchCountResult(
                event.searchId(),
                event.search(),
                count
        );
    }
}