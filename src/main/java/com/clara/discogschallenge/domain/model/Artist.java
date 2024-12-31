package com.clara.discogschallenge.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Artist {

    private Long id;
    private String discogsId;
    private String name;
    private List<Release> releases = new ArrayList<>();
}
