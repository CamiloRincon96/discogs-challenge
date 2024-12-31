package com.clara.discogschallenge.infrastructure.adapter.repository;

import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {

}
