package com.clara.discogschallenge.infrastructure.adapter.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.model.Release;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DiscogsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HttpHeaders headers;

    @Value("${discogs.api.key}")
    private String consumerKey;

    @Value("${discogs.api.secret}")
    private String consumerSecret;

    public DiscogsClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.headers = new HttpHeaders();
        this.headers.set("User-Agent", "DiscogsChallenge/1.0");
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "DiscogsChallenge/1.0");
        headers.set("Authorization", 
            String.format("Discogs key=%s, secret=%s", consumerKey, consumerSecret));
        return headers;
    }

    public List<Artist> searchArtists(String query, int page, int perPage) {
        String url = String.format("https://api.discogs.com/database/search?q=%s&type=artist&page=%d&per_page=%d",
                query, page, perPage);
        
        ResponseEntity<String> response = makeRequest(url);
        return response.getStatusCode().is2xxSuccessful() 
            ? parseArtistsFromSearchJson(response.getBody())
            : Collections.emptyList();
    }

    public List<Release> getArtistReleases(String artistId, int page, int perPage) {
        String url = String.format("https://api.discogs.com/artists/%s/releases?page=%d&per_page=%d", 
                artistId, page, perPage);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "DiscogsChallenge/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Release> releases = parseReleasesFromJson(response.getBody());
                releases.forEach(this::enrichReleaseWithDetails);
                return releases;
            }
        } catch (Exception e) {
            System.err.println("Error fetching releases: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private ResponseEntity<String> makeRequest(String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders()),
                String.class
        );
    }

    private void enrichReleaseWithDetails(Release release) {
        String url = String.format("https://api.discogs.com/releases/%s", release.getDiscogsId());
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "DiscogsChallenge/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                
                JsonNode genresNode = root.get("genres");
                if (genresNode != null && genresNode.isArray() && genresNode.size() > 0) {
                    release.setGenre(genresNode.get(0).asText());
                } else {
                    JsonNode stylesNode = root.get("styles");
                    if (stylesNode != null && stylesNode.isArray() && stylesNode.size() > 0) {
                        release.setGenre(stylesNode.get(0).asText());
                    }
                }
                
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Error enriching release " + release.getDiscogsId() + ": " + e.getMessage());
        }
    }

    private List<Artist> parseArtistsFromSearchJson(String json) {
        List<Artist> artists = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.get("results");
            if (results != null && results.isArray()) {
                for (JsonNode node : results) {
                    if (node.has("type") && "artist".equals(node.get("type").asText())) {
                        Artist artist = new Artist();
                        artist.setDiscogsId(node.get("id").asText());
                        artist.setName(node.get("title").asText());
                        artists.add(artist);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return artists;
    }

    private List<Release> parseReleasesFromJson(String json) {
        List<Release> releases = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode releasesArray = root.get("releases");
            if (releasesArray != null && releasesArray.isArray()) {
                for (JsonNode node : releasesArray) {
                    Release release = new Release();
                    release.setDiscogsId(node.get("id").asText());
                    release.setTitle(node.get("title").asText());
                    
                    if (node.has("year") && !node.get("year").isNull()) {
                        release.setReleaseYear(node.get("year").asInt());
                    }
                    
                    JsonNode stylesNode = node.get("style");
                    if (stylesNode != null && stylesNode.isArray() && stylesNode.size() > 0) {
                        release.setGenre(stylesNode.get(0).asText());
                    } else {
                        JsonNode genreNode = node.get("genre");
                    if (genreNode != null && genreNode.isArray() && genreNode.size() > 0) {
                            release.setGenre(genreNode.get(0).asText());
                        }
                    }
                    
                    releases.add(release);
                }
            }
        } catch (IOException e) {
            System.err.println("Error parsing releases JSON: " + e.getMessage());
        }
        return releases;
    }
}
