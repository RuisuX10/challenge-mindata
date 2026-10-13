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

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        List<Integer> ages = List.of(25, -5);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                ages
            )
        );

        assertEquals(
            "Las edades deben ser mayores o iguales a cero",
            exception.getMessage()
        );
    }


    @Test
    void shouldRejectInvalidDates() {

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(2);
        List<Integer> ages = List.of(25, 30);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                ages
            )
        );

        assertEquals(
            "El checkIn debe ser anterior al checkOut",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectPastCheckInDate() {

        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(1);
        List<Integer> ages = List.of(25, 30);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                ages
            )
        );

        assertEquals(
            "El checkIn no puede ser anterior a la fecha actual",
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
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        List<Integer> ages = List.of(25, 30);

        for (String hotelId : invalidIds) {

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    hotelId,
                    checkIn,
                    checkOut,
                    ages
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

        LocalDate validDate = LocalDate.now().plusDays(2);
        List<Integer> ages = List.of(25);

        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    "HOTEL123", null, validDate, ages
                )
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new HotelAvailabilitySearch(
                    "HOTEL123", validDate, null, ages
                )
            )
        );
    }

    @Test
    void shouldRejectEmptyAges() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        List<Integer> emptyAges = List.of();

        assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                emptyAges
            )
        );
    }

    @Test
    void shouldRejectNullAges() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                null
            )
        );

        assertEquals("La lista de edades es obligatoria", exception.getMessage());
    }

    @Test
    void shouldRejectNullAge() {

        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        List<Integer> ages = Arrays.asList(25, null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new HotelAvailabilitySearch(
                "HOTEL123",
                checkIn,
                checkOut,
                ages
            )
        );

        assertEquals(
            "Las edades deben ser mayores o iguales a cero",
            exception.getMessage()
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
        List<Integer> immutableAges = search.ages();

        assertAll(
            () -> assertEquals(List.of(25, 30), search.ages()),
            () -> assertThrows(
                UnsupportedOperationException.class,
                () -> immutableAges.add(50)
            )
        );
    }
    
}