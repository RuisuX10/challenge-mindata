package com.mindata.challenge.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "hotel_searches",
    indexes = @Index(
        name = "idx_hotel_search_match",
        columnList = "hotel_id, check_in, check_out, ages"
    )
)
public class HotelSearchJpaEntity {

    @Id
    private String searchId;

    @Column(nullable = false)
    private String hotelId;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private String ages;

    protected HotelSearchJpaEntity() {
    }

    public HotelSearchJpaEntity(
            String searchId,
            String hotelId,
            LocalDate checkIn,
            LocalDate checkOut,
            String ages) {

        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
    }

    public String getSearchId() {
        return searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public String getAges() {
        return ages;
    }
}