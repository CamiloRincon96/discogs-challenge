package com.clara.discogschallenge.domain.model;

import lombok.Data;

@Data
public class Release {

    private String discogsId;
    private String title;
    private Integer releaseYear;
    private String genre;
    private Long artistId;
}
