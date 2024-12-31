package com.clara.discogschallenge.domain.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.service.mapper.DiscographyMapper;
import com.clara.discogschallenge.infrastructure.adapter.repository.ArtistRepository;
import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ArtistEntity;

@ExtendWith(MockitoExtension.class)
class DiscographyServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private DiscographyMapper mapper;

    @InjectMocks
    private DiscographyService discographyService;

    @Test
    void getDiscographyByArtistIdWhenExistsShouldReturnArtist() {
        Long artistId = 1L;
        ArtistEntity artistEntity = new ArtistEntity();
        Artist expectedArtist = new Artist();
        
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artistEntity));
        when(mapper.toArtist(artistEntity)).thenReturn(expectedArtist);

        Artist result = discographyService.getDiscographyByArtistId(artistId);

        assertNotNull(result);
        assertEquals(expectedArtist, result);
    }

    @Test
    void getDiscographyByArtistIdWhenNotExistsShouldThrowException() {
        Long artistId = 1L;
        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            discographyService.getDiscographyByArtistId(artistId)
        );
    }
}
