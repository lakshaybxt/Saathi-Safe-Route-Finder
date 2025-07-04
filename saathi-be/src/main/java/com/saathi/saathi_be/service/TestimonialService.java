package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.TestimonialDto;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface TestimonialService {
    Testimonial reportArea(TestimonialDto testimonialDto, User loginUser);
    List<Testimonial> getTestimonialByPlaceId(UUID placeId);
}
