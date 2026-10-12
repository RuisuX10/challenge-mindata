package com.mindata.challenge.infrastructure.config;

import com.mindata.challenge.application.port.in.CountSearchUseCase;
import com.mindata.challenge.application.port.in.SearchUseCase;
import com.mindata.challenge.application.service.CountSearchService;
import com.mindata.challenge.application.service.SearchService;
import com.mindata.challenge.domain.port.out.SearchEventPublisherPort;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public SearchUseCase searchUseCase(
            SearchEventPublisherPort publisherPort) {

        return new SearchService(publisherPort);
    }

    @Bean
    public CountSearchUseCase countSearchUseCase(
            SearchRepositoryPort repositoryPort
    ) {
        return new CountSearchService(repositoryPort);
    }
}