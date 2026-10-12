package com.mindata.challenge.domain.model;

import java.time.LocalDate;
import java.util.List;

public record HotelAvailabilitySearch(

        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages

) {

    public HotelAvailabilitySearch {

        if (hotelId == null || hotelId.isBlank()) {
            throw new IllegalArgumentException(
                    "El hotelId es obligatorio"
            );
        }

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias"
            );
        }

        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException(
                    "El checkIn debe ser anterior al checkOut"
            );
        }

        if (ages == null || ages.isEmpty()) {
            throw new IllegalArgumentException(
                    "La lista de edades es obligatoria"
            );
        }

        if (ages.stream().anyMatch(age -> age == null || age < 0)) {
            throw new IllegalArgumentException(
                    "Las edades deben ser mayores o iguales a cero"
            );
        }

        ages = List.copyOf(ages);
    }
}