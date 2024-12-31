package com.clara.discogschallenge.domain.service;

import java.util.List;

import com.clara.discogschallenge.domain.service.mapper.DiscographyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.model.Release;
import com.clara.discogschallenge.infrastructure.adapter.repository.ArtistRepository;
import com.clara.discogschallenge.infrastructure.adapter.repository.ReleaseRepository;
import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ArtistEntity;

@Service
public class DiscographyService {

    private final ArtistRepository artistRepository;
    private final ReleaseRepository releaseRepository;
    private final DiscographyMapper mapper;

    public DiscographyService(
            ArtistRepository artistRepository,
            ReleaseRepository releaseRepository,
            DiscographyMapper mapper) {
        this.artistRepository = artistRepository;
        this.releaseRepository = releaseRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Artist saveArtistWithReleases(Artist artist) {
        ArtistEntity artistEntity = mapper.toArtistEntity(artist);
        ArtistEntity savedArtist = artistRepository.save(artistEntity);
        
        savedArtist.getReleases().clear();
        
        artist.getReleases().stream()
                .filter(r -> r.getDiscogsId() != null && r.getTitle() != null)
                .map(release -> mapper.toReleaseEntity(release, savedArtist))
                .forEach(releaseEntity -> savedArtist.getReleases().add(releaseEntity));
        
        ArtistEntity finalEntity = artistRepository.save(savedArtist);
        return mapper.toArtist(finalEntity);
    }

    public Artist getDiscographyByArtistId(Long artistId) {
        return artistRepository.findById(artistId)
                .map(mapper::toArtist)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
    }

    public List<Release> getReleasesByArtist(Long artistId) {
        return releaseRepository.findAllByArtistIdOrderByReleaseYear(artistId).stream()
                .map(mapper::toRelease)
                .toList();
    }
}
