package com.clara.discogschallenge.infrastructure.adapter.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clara.discogschallenge.domain.service.ComparisonService;

@WebMvcTest(ArtistComparisonController.class)
class ArtistComparisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComparisonService comparisonService;

    @Test
    @WithMockUser
    void compareArtistsWithValidIdsShouldReturnOk() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        when(comparisonService.compareArtists(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/comparison")
                .param("artistIds", "1,2"))
                .andExpect(status().isOk());
    }

    @Test
    void compareArtistsWithoutAuthShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/comparison")
                .param("artistIds", "1,2"))
                .andExpect(status().isUnauthorized());
    }
}
