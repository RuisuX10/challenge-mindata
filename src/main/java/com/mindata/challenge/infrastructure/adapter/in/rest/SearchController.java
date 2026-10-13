package com.mindata.challenge.infrastructure.adapter.in.rest;

import com.mindata.challenge.application.port.in.SearchUseCase;
import com.mindata.challenge.application.port.in.CountSearchUseCase;
import com.mindata.challenge.application.port.in.SearchCountResult;
import com.mindata.challenge.domain.model.HotelAvailabilitySearch;
import com.mindata.challenge.infrastructure.adapter.in.rest.dto.SearchRequest;
import com.mindata.challenge.infrastructure.adapter.in.rest.dto.SearchCountResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Hotel searches", description = "Create hotel availability searches and retrieve their counts")
public class SearchController {

    private final SearchUseCase searchUseCase;
    private final CountSearchUseCase countSearchUseCase;

    public SearchController(
            SearchUseCase searchUseCase,
            CountSearchUseCase countSearchUseCase
    ) {
        this.searchUseCase = searchUseCase;
        this.countSearchUseCase = countSearchUseCase;
    }

    @PostMapping("/search")
        @Operation(
            summary = "Create a hotel availability search",
            description = "Validates the search, publishes it to Kafka, and returns its unique searchId."
        )
        @ApiResponse(responseCode = "202", description = "Search accepted")
        @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content)
        @ApiResponse(responseCode = "500", description = "Search could not be published", content = @Content)
    public ResponseEntity<Map<String, String>> createSearch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Hotel, stay dates, and guest ages",
                required = true,
                content = @Content(schema = @Schema(implementation = SearchRequest.class))
            )
            @RequestBody SearchRequest request
    ) {

        HotelAvailabilitySearch search = request.toDomain();

        String searchId = searchUseCase.createSearch(search);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of("searchId", searchId));
    }

    @GetMapping("/count")
        @Operation(
            summary = "Count matching searches",
            description = "Returns the stored search and the number of searches with identical fields, including the same age order."
        )
        @ApiResponse(
            responseCode = "200",
            description = "Search and matching count",
            content = @Content(schema = @Schema(implementation = SearchCountResponse.class))
        )
        @ApiResponse(responseCode = "400", description = "Missing searchId", content = @Content)
        @ApiResponse(responseCode = "404", description = "Search not found", content = @Content)
    public ResponseEntity<SearchCountResponse> countSearches(
            @Parameter(description = "ID returned by POST /search", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam String searchId
    ) {
        SearchCountResult result = countSearchUseCase.countSearches(searchId);

        SearchCountResponse response = SearchCountResponse.fromResult(result);

        return ResponseEntity.ok(response);
    }
}