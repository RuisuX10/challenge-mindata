package com.mindata.challenge.domain.port.out;

import com.mindata.challenge.domain.model.SearchCreatedEvent;

public interface SearchEventPublisherPort {

    void publish(SearchCreatedEvent event);

}