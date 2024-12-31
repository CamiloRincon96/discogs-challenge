package com.clara.discogschallenge.infrastructure.adapter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clara.discogschallenge.domain.service.ComparisonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/comparison")
@Tag(name = "Comparison", description = "Endpoints for comparing artists")
public class ArtistComparisonController {

    private final ComparisonService comparisonService;

    public ArtistComparisonController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @Operation(summary = "Compare artists",
            description = "Compares multiple artists based on their releases, active years, and genres. " +
                    "First save some artists using the /api/discography/save endpoint, " +
                    "then use their internal IDs here.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Comparison successful",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "comparison": {
                        "artist_1": {
                          "name": "Shakira",
                          "totalReleases": 45,
                          "activeYears": 32,
                          "firstRelease": 1991,
                          "lastRelease": 2023,
                          "genres": {
                            "Pop": 20,
                            "Latin": 15,
                            "Rock": 10
                          }
                        },
                        "artist_2": {
                          "name": "Karol G",
                          "totalReleases": 25,
                          "activeYears": 8,
                          "firstRelease": 2015,
                          "lastRelease": 2023,
                          "genres": {
                            "Reggaeton": 15,
                            "Latin Pop": 5,
                            "Urban": 5
                          }
                        },
                        "artist_3": {
                          "name": "Michael Jackson",
                          "totalReleases": 50,
                          "activeYears": 40,
                          "firstRelease": 1969,
                          "lastRelease": 2009,
                          "genres": {
                            "Pop": 25,
                            "Soul": 15,
                            "R&B": 10
                          }
                        }
                      },
                      "summary": {
                        "mostReleases": {
                          "artist": "Michael Jackson",
                          "count": 50
                        },
                        "longestCareer": {
                          "artist": "Michael Jackson",
                          "years": 40,
                          "from": 1969,
                          "to": 2009
                        }
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping
    public Map<String, Object> compareArtists(
            @Parameter(description = "List of internal artist IDs", example = "1,2,3")
            @RequestParam List<Long> artistIds
    ) {
        return comparisonService.compareArtists(artistIds);
    }
}
