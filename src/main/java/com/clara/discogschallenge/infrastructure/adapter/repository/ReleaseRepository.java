package com.clara.discogschallenge.infrastructure.adapter.repository;

import com.clara.discogschallenge.infrastructure.adapter.repository.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {

    List<ReleaseEntity> findAllByArtistIdOrderByReleaseYear(Long artistId);
}
