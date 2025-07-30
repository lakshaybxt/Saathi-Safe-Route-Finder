package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {

    @Query("SELECT p.riskColor FROM Place p WHERE LOWER(p.locality) = LOWER(:city)")
    Optional<String> findRiskColorByCityName(@Param("city") String city);

    @Query(value = """
        SELECT p.risk_color
        FROM places p
        WHERE EXISTS (
            SELECT 1
            FROM unnest(:parts) AS part
            WHERE LOWER(p.locality) LIKE CONCAT('%', part, '%')
        )
        LIMIT 1
    """, nativeQuery = true)
    String findRiskColorByLocalityContaining(@Param("parts") String[] parts);

    List<Place> findAllByOrderByLocalityAsc();

    Optional<Place> findPlaceByLocalityAndState(String name, String state);

    List<Place> findByRiskColorAndStateIgnoreCase(String color, String state);

    @Query(value = """
        SELECT p.risk_color AS riskColor, COUNT(*) AS count
        FROM places p
        WHERE LOWER(p.state) = :state
        GROUP BY p.risk_color
    """, nativeQuery = true)
    List<Object[]> countRiskColorGroupByState(@Param("state") String state);

    @Query("SELECT COUNT(p) FROM Place p")
    long countPlaces();
}
