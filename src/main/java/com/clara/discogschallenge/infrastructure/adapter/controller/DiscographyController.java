package com.clara.discogschallenge.infrastructure.adapter.controller;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.model.Release;
import com.clara.discogschallenge.domain.service.DiscographyService;
import com.clara.discogschallenge.domain.service.SearchArtistService;
import com.clara.discogschallenge.infrastructure.adapter.client.DiscogsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/discography")
@Tag(name = "Discography", description = "Endpoints for managing artist discographies")
public class DiscographyController {

    private final DiscographyService discographyService;
    private final SearchArtistService searchArtistService;
    private final DiscogsClient discogsClient;

    public DiscographyController(
            SearchArtistService searchArtistService,
            DiscographyService discographyService,
            DiscogsClient discogsClient) {
        this.searchArtistService = searchArtistService;
        this.discographyService = discographyService;
        this.discogsClient = discogsClient;
    }

    @Operation(summary = "Save artist discography",
            description = "Searches for an artist on Discogs and saves their discography to the database")
    @PostMapping("/save")
    public Artist saveDiscography(
            @Parameter(description = "Artist name or Discogs ID", example = "madonna")
            @RequestParam String artistDiscogsIdOrName,
            @Parameter(description = "Page number", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Results per page", example = "50")
            @RequestParam(defaultValue = "50") int perPage
    ) {
        List<Artist> found = searchArtistService.searchArtists(artistDiscogsIdOrName, page, perPage);

        if (found.isEmpty()) {
            throw new RuntimeException("No artists found in Discogs");
        }
        Artist artist = found.get(0);
        
        List<Release> releases = discogsClient.getArtistReleases(artist.getDiscogsId(), page, perPage);
        artist.setReleases(releases);
        
        return discographyService.saveArtistWithReleases(artist);
    }

    @Operation(summary = "Get complete discography",
            description = "Retrieves the complete discography of an artist using their internal ID")
    @GetMapping("/{artistId}")
    public Artist getDiscography(
            @Parameter(description = "Internal artist ID", example = "1")
            @PathVariable Long artistId
    ) {
        return discographyService.getDiscographyByArtistId(artistId);
    }

    @Operation(summary = "Get releases sorted by year",
            description = "Retrieves only the releases of an artist sorted by year")
    @GetMapping("/{artistId}/releases")
    public List<Release> getReleases(
            @Parameter(description = "Internal artist ID", example = "1")
            @PathVariable Long artistId
    ) {
        return discographyService.getReleasesByArtist(artistId);
    }
}