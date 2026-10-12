package com.mindata.challenge.domain.model;

public record SearchCreatedEvent(
    String searchId,
    HotelAvailabilitySearch search
) {}