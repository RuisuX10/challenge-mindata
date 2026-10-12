package com.mindata.challenge.infrastructure.adapter.in.rest;

import com.mindata.challenge.application.port.in.CountSearchUseCase;
import com.mindata.challenge.application.port.in.SearchUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    private SearchUseCase searchUseCase;
    private CountSearchUseCase countSearchUseCase;

    @BeforeEach
    void setUp() {

        searchUseCase = mock(SearchUseCase.class);
        countSearchUseCase = mock(CountSearchUseCase.class);

        SearchController controller = new SearchController(
                searchUseCase,
                countSearchUseCase
        );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnBadRequestWhenCheckInIsAfterCheckOut()
            throws Exception {

        String json = """
                {
                    "hotelId": "HOTEL123",
                    "checkIn": "20/10/2026",
                    "checkOut": "15/10/2026",
                    "ages": [20, 30, 5]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("El checkIn debe ser anterior al checkOut"));

        verifyNoInteractions(searchUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenAgeIsNegative()
            throws Exception {

        String json = """
                {
                    "hotelId": "HOTEL123",
                    "checkIn": "15/10/2026",
                    "checkOut": "20/10/2026",
                    "ages": [20, -5]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Las edades deben ser mayores o iguales a cero"));

        verifyNoInteractions(searchUseCase);
    }

    @Test
    void shouldReturnBadRequestForInvalidJson()
            throws Exception {

        String invalidJson = """
                {
                    "hotelId": "HOTEL123",
                    "checkIn": "fecha-invalida",
                    "checkOut": "20/10/2026",
                    "ages": [20, 30]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("JSON inválido o campos con formato incorrecto"));

        verifyNoInteractions(searchUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenSearchIdIsMissing()
            throws Exception {

        mockMvc.perform(get("/count"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Falta el parámetro obligatorio: searchId"));

        verifyNoInteractions(countSearchUseCase);
    }

    @Test
    void shouldReturnNotFoundWhenSearchDoesNotExist()
            throws Exception {

        when(countSearchUseCase.countSearches("invalid-id"))
                .thenThrow(new NoSuchElementException(
                        "No existe una búsqueda con el searchId: invalid-id"
                ));

        mockMvc.perform(get("/count")
                        .param("searchId", "invalid-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("No existe una búsqueda con el searchId: invalid-id"));
    }
}