package com.mindata.challenge.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelAvailabilitySearchTest {

    @Test
    void shouldCreateValidSearch() {

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
                "HOTEL123",
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20),
                List.of(25, 30, 5)
        );

        assertAll(
                () -> assertEquals("HOTEL123", search.hotelId()),
                () -> assertEquals(
                        LocalDate.of(2026, 10, 15),
                        search.checkIn()
                ),
                () -> assertEquals(3, search.ages().size())
        );
    }

    @Test
    void shouldRejectNegativeAge() {

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20),
                List.of(25, -5)
            )
        );

        assertEquals(
            "Las edades deben ser mayores o iguales a cero",
            exception.getMessage()
        );
    }


    @Test
    void shouldRejectInvalidDates() {

        LocalDate checkIn = LocalDate.of(2026, 10, 20);
        LocalDate checkOut = LocalDate.of(2026, 10, 15);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                List.of(25, 30)
            )
        );

        assertEquals(
            "El checkIn debe ser anterior al checkOut",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectInvalidHotelId() {

        List<String> invalidIds = Arrays.asList(
            null,
            "",
            "   "
        );

        for (String hotelId : invalidIds) {

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    hotelId,
                    LocalDate.of(2026, 10, 15),
                    LocalDate.of(2026, 10, 20),
                    List.of(25, 30)
                )
            );

            assertEquals(
                "El hotelId es obligatorio",
                exception.getMessage()
            );
        }
    }

    @Test
    void shouldRejectNullDates() {

        LocalDate validDate = LocalDate.of(2026, 10, 15);

        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    "HOTEL123", null, validDate, List.of(25)
                )
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    "HOTEL123", validDate, null, List.of(25)
                )
            )
        );
    }

    @Test
    void shouldRejectEmptyAges() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20),
                List.of()
            )
        );
    }

    @Test
    void shouldProtectAgesFromModification() {

        List<Integer> ages = new ArrayList<>(List.of(25, 30));

        HotelAvailabilitySearch search = new HotelAvailabilitySearch(
            "HOTEL123",
            LocalDate.of(2026, 10, 15),
            LocalDate.of(2026, 10, 20),
            ages
        );

        ages.add(40);

        assertAll(
            () -> assertEquals(List.of(25, 30), search.ages()),
            () -> assertThrows(
                UnsupportedOperationException.class,
                () -> search.ages().add(50)
            )
        );
    }
    
}