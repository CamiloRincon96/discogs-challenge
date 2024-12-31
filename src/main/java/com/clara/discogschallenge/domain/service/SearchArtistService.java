package com.clara.discogschallenge.domain.service;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.infrastructure.adapter.client.DiscogsClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchArtistService {

    private final DiscogsClient discogsClient;

    public SearchArtistService(DiscogsClient discogsClient) {
        this.discogsClient = discogsClient;
    }

    @Cacheable(value = "artistSearchCache", key = "#query.concat('-').concat(#page).concat('-').concat(#perPage)")
    public List<Artist> searchArtists(String query, int page, int perPage) {
        return discogsClient.searchArtists(query, page, perPage);
    }
}
