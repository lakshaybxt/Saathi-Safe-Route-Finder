package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.TestimonialDto;
import com.saathi.saathi_be.domain.entity.Place;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.PlaceRepository;
import com.saathi.saathi_be.repository.TestimonialRepository;
import com.saathi.saathi_be.repository.UserRepository;
import com.saathi.saathi_be.service.TestimonialService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final PlaceRepository placeRepository;

    @Override
    public Testimonial reportArea(TestimonialDto testimonialDto, User loginUser) {
        Place place = placeRepository.findById(testimonialDto.getPlaceId())
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + testimonialDto.getPlaceId()));

        place.setRiskColor(calculateRiskColor(place.getId(), testimonialDto));
        placeRepository.save(place);

        Testimonial testimonial = Testimonial.builder()
                .rating(testimonialDto.getRating())
                .comment(testimonialDto.getComment())
                .tips(testimonialDto.getTips())
                .user(loginUser)
                .place(place)
                .build();

        return testimonialRepository.save(testimonial);
    }

    @Override
    public List<Testimonial> getTestimonialByPlaceId(UUID placeId) {
        return testimonialRepository.findAllByPlace_Id(placeId);
    }

    @Override
    public List<Testimonial> getTestimonialByUser(User user) {
        return testimonialRepository.findAllByUser(user);
    }

    @Override
    public Testimonial getTestimonialById(UUID id) {
        return testimonialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial not found with id: " + id));
    }

    @Override
    public Testimonial updateTestimonial(UUID id, TestimonialDto testimonialDto) {
        Testimonial existingTestimonial = getTestimonialById(id);

        if(!existingTestimonial.getPlace().getId().equals(testimonialDto.getPlaceId())) {
            Place place = placeRepository.findById(testimonialDto.getPlaceId())
                    .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + testimonialDto.getPlaceId()));

            place.setRiskColor(calculateRiskColor(place.getId(), testimonialDto));
            placeRepository.save(place);

            existingTestimonial.setPlace(place);
        }

        existingTestimonial.setRating(testimonialDto.getRating());
        existingTestimonial.setComment(testimonialDto.getComment());
        existingTestimonial.setTips(testimonialDto.getTips());

        return testimonialRepository.save(existingTestimonial);
    }

    @Override
    public void deleteTestimonial(UUID id) {
        Testimonial existingTestimonial = getTestimonialById(id);
        testimonialRepository.delete(existingTestimonial);
    }

    @Override
    public long getReviewers() {
        return testimonialRepository.count();
    }

    private String calculateRiskColor(UUID placeId, TestimonialDto testimonialDto) {
        List<Testimonial> testimonials = getTestimonialByPlaceId(placeId);

        List<Integer> ratings = testimonials.stream()
                .map(Testimonial::getRating)
                .collect(Collectors.toList());
        ratings.add(testimonialDto.getRating());

        // Average rating can be in place
        double avgRating = ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        String riskColor;
        if(avgRating >= 4.5) riskColor = "green";
        else if (avgRating >= 3.0) riskColor = "yellow";
        else riskColor = "red";

        return riskColor;
    }
}
