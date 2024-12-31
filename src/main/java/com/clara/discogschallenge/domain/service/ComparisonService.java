package com.clara.discogschallenge.domain.service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clara.discogschallenge.domain.model.Artist;
import com.clara.discogschallenge.domain.model.Release;

@Service
public class ComparisonService {

    private final DiscographyService discographyService;

    public ComparisonService(DiscographyService discographyService) {
        this.discographyService = discographyService;
    }

    public Map<String, Object> compareArtists(List<Long> artistIds) {
        List<Artist> artists = artistIds.stream()
                .map(id -> {
                    try {
                        return discographyService.getDiscographyByArtistId(id);
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Artist with ID " + id + " not found");
                    }
                })
                .toList();

        if (artists.isEmpty()) {
            throw new RuntimeException("No artists found for comparison");
        }

        Map<String, Object> comparisonResult = new LinkedHashMap<>();
        Map<String, Object> artistsComparison = new LinkedHashMap<>();

        for (Artist artist : artists) {
            Map<String, Object> artistStats = new LinkedHashMap<>();
            
            int releasesCount = artist.getReleases().size();
            
            OptionalInt minYear = artist.getReleases().stream()
                    .filter(r -> r.getReleaseYear() != null)
                    .mapToInt(Release::getReleaseYear)
                    .min();
            OptionalInt maxYear = artist.getReleases().stream()
                    .filter(r -> r.getReleaseYear() != null)
                    .mapToInt(Release::getReleaseYear)
                    .max();
            
            int activeYears = 0;
            if (minYear.isPresent() && maxYear.isPresent()) {
                activeYears = maxYear.getAsInt() - minYear.getAsInt();
                artistStats.put("firstRelease", minYear.getAsInt());
                artistStats.put("lastRelease", maxYear.getAsInt());
            }
            
            Map<String, Long> genreCounts = artist.getReleases().stream()
                    .filter(r -> r.getGenre() != null)
                    .collect(Collectors.groupingBy(
                            Release::getGenre,
                            Collectors.counting()
                    ));

            Map<String, Long> sortedGenres = genreCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            artistStats.put("name", artist.getName());
            artistStats.put("totalReleases", releasesCount);
            artistStats.put("activeYears", activeYears);
            artistStats.put("genres", sortedGenres);

            artistsComparison.put("artist_" + artist.getId(), artistStats);
        }

        comparisonResult.put("comparison", artistsComparison);
        
        addComparativeSummary(comparisonResult, artists);

        return comparisonResult;
    }

    private void addComparativeSummary(Map<String, Object> result, List<Artist> artists) {
        Map<String, Object> summary = new LinkedHashMap<>();
        
        Artist mostProlifictArtist = artists.stream()
                .max(Comparator.comparingInt(a -> a.getReleases().size()))
                .orElse(null);
        
        if (mostProlifictArtist != null) {
            summary.put("mostReleases", Map.of(
                "artist", mostProlifictArtist.getName(),
                "count", mostProlifictArtist.getReleases().size()
            ));
        }

        Artist longestCareerArtist = artists.stream()
                .max(Comparator.comparingInt(a -> {
                    OptionalInt max = a.getReleases().stream()
                            .mapToInt(Release::getReleaseYear)
                            .max();
                    OptionalInt min = a.getReleases().stream()
                            .mapToInt(Release::getReleaseYear)
                            .min();
                    return max.orElse(0) - min.orElse(0);
                }))
                .orElse(null);

        if (longestCareerArtist != null) {
            OptionalInt max = longestCareerArtist.getReleases().stream()
                    .mapToInt(Release::getReleaseYear)
                    .max();
            OptionalInt min = longestCareerArtist.getReleases().stream()
                    .mapToInt(Release::getReleaseYear)
                    .min();
            
            summary.put("longestCareer", Map.of(
                "artist", longestCareerArtist.getName(),
                "years", max.orElse(0) - min.orElse(0),
                "from", min.orElse(0),
                "to", max.orElse(0)
            ));
        }

        result.put("summary", summary);
    }
}
