package com.clara.discogschallenge.domain.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComparisonServiceTest {

    @Mock
    private DiscographyService discographyService;

    private ComparisonService comparisonService;

    @BeforeEach
    void setUp() {
        comparisonService = new ComparisonService(discographyService);
    }

    @Test
    void compareArtistsWithEmptyListShouldThrowException() {
        Exception exception = assertThrows(RuntimeException.class, () ->
            comparisonService.compareArtists(List.of())
        );
        assertEquals("No artists found for comparison", exception.getMessage());
    }
} 
