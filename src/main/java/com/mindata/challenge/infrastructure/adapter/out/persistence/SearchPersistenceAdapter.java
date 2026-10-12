package com.mindata.challenge.infrastructure.adapter.out.persistence;

import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.domain.model.SearchCreatedEvent;
import com.mindata.challenge.domain.port.out.SearchRepositoryPort;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SearchPersistenceAdapter implements SearchRepositoryPort {

    private final HotelSearchJpaRepository jpaRepository;

    public SearchPersistenceAdapter(HotelSearchJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(SearchCreatedEvent event) {

        HotelAvailabilitySearch search = event.search();

        String ages = search.ages().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        HotelSearchJpaEntity entity = new HotelSearchJpaEntity(
                event.searchId(),
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                ages
        );

        jpaRepository.save(entity);
    }

    @Override
    public long count(HotelAvailabilitySearch search) {

        String ages = search.ages().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return jpaRepository.countMatchingSearches(
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                ages
        );
    }

    @Override
    public Optional<SearchCreatedEvent> findById(String searchId) {

        return jpaRepository.findById(searchId)
                .map(entity -> {

                    List<Integer> ages = Arrays.stream(
                                entity.getAges().split(",")
                            )
                            .map(Integer::parseInt)
                            .toList();

                    HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                            entity.getHotelId(),
                            entity.getCheckIn(),
                            entity.getCheckOut(),
                            ages
                    );

                    return new SearchCreatedEvent(
                            entity.getSearchId(),
                            search
                    );
                });
    }
}