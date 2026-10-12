package com.mindata.challenge.infrastructure.adapter.out.persistence;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelSearchJpaRepository
        extends JpaRepository<HotelSearchJpaEntity, String> {

    @Query("""
        SELECT COUNT(h)
        FROM HotelSearchJpaEntity h
        WHERE h.hotelId = :hotelId
          AND h.checkIn = :checkIn
          AND h.checkOut = :checkOut
          AND h.ages = :ages
        """)
    long countMatchingSearches(
            @Param("hotelId") String hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("ages") String ages
    );
}