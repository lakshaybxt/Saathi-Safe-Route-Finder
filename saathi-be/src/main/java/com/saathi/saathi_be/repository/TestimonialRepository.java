package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {
    List<Testimonial> findTop5ByPlace_NameInOrderByRatingDesc(List<String> placeNames);
    List<Testimonial> findAllByPlace_Id(UUID placeId);
}
