package com.clara.discogschallenge.infrastructure.adapter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.service.SearchArtistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artist Search", description = "Endpoints for searching artists in Discogs")
public class ArtistSearchController {

    private final SearchArtistService searchArtistService;

    public ArtistSearchController(SearchArtistService searchArtistService) {
        this.searchArtistService = searchArtistService;
    }

    @Operation(
        summary = "Search artists in Discogs",
        description = "Search for artists in the Discogs database"
    )
    @GetMapping("/search")
    public List<Artist> searchArtists(
            @Parameter(
                description = "Search query",
                example = "shakira"
            )
            @RequestParam String query,
            
            @Parameter(description = "Page number for pagination")
            @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "Results per page (max 100)")
            @RequestParam(defaultValue = "10") int perPage
    ) {
        return searchArtistService.searchArtists(query, page, perPage);
    }
}