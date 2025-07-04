package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {
    // Inside exists is a boolean check
    @Query(value = """
        SELECT t.*
        FROM testimonials t
        JOIN places p ON t.place_id = p.id
        WHERE EXISTS (
            SELECT 1
            FROM unnest(:parts) AS part
            WHERE LOWER(p.locality) LIKE CONCAT('%', part, '%')     
        )  
        ORDER BY t.rating DESC 
        LIMIT 5
    """, nativeQuery = true)
    List<Testimonial> findTop5ByPlace_LocalityInOrderByRatingDesc(@Param("parts") String[] parts);
    List<Testimonial> findAllByPlace_Id(UUID placeId);
    List<Testimonial> findAllByUser(User user);
}
