package com.clara.discogschallenge.domain.service.mapper;

import org.springframework.stereotype.Component;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.model.Release;
import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ArtistEntity;
import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ReleaseEntity;

@Component
public class DiscographyMapper {
    
    public Artist toArtist(ArtistEntity entity) {
        if (entity == null) return null;
        
        Artist artist = new Artist();
        artist.setId(entity.getId());
        artist.setDiscogsId(entity.getDiscogsId());
        artist.setName(entity.getName());
        
        if (entity.getReleases() != null) {
            artist.setReleases(entity.getReleases().stream()
                    .map(this::toRelease)
                    .toList());
        }
        
        return artist;
    }
    
    public Release toRelease(ReleaseEntity entity) {
        if (entity == null) return null;
        
        Release release = new Release();
        release.setDiscogsId(entity.getDiscogsId());
        release.setTitle(entity.getTitle());
        release.setReleaseYear(entity.getReleaseYear());
        release.setGenre(entity.getGenre());
        release.setArtistId(entity.getArtist().getId());
        
        return release;
    }
    
    public ArtistEntity toArtistEntity(Artist artist) {
        if (artist == null) return null;
        
        ArtistEntity entity = new ArtistEntity();
        entity.setDiscogsId(artist.getDiscogsId());
        entity.setName(artist.getName());
        return entity;
    }
    
    public ReleaseEntity toReleaseEntity(Release release, ArtistEntity artist) {
        if (release == null) return null;
        
        ReleaseEntity entity = new ReleaseEntity();
        entity.setDiscogsId(release.getDiscogsId());
        entity.setTitle(release.getTitle());
        entity.setReleaseYear(release.getReleaseYear());
        entity.setGenre(release.getGenre());
        entity.setArtist(artist);
        
        return entity;
    }
} 