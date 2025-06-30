package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {
    @Query("SELECT p.riskColor FROM Place p WHERE LOWER(p.name) = LOWER(:city)")
    Optional<String> findRiskColorByCityName(@Param("city") String city);
}
