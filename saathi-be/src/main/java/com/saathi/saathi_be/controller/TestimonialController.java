package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.TestimonialDto;
import com.saathi.saathi_be.domain.dto.response.TestimonialResponse;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.mapper.TestimonialMapper;
import com.saathi.saathi_be.service.TestimonialService;
import com.saathi.saathi_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final UserService userService;
    private final TestimonialMapper mapper;

    @PostMapping
    public ResponseEntity<TestimonialResponse> reportArea(
            @Valid @RequestBody TestimonialDto testimonialDto,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);
        Testimonial testimonial = testimonialService.reportArea(testimonialDto, loggedInUser);

//        TestimonialResponse response = TestimonialResponse.builder()
//                .comment(testimonial.getComment())
//                .tips(testimonial.getTips())
//                .rating(testimonial.getRating())
//                .city(testimonial.getPlace().getLocality())
//                .build();
        TestimonialResponse response = mapper.toResponse(testimonial);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/places/{placeId}/testimonials")
    public ResponseEntity<List<TestimonialResponse>> getTestimonialByPlace(@PathVariable UUID placeId) {
        List<Testimonial> testimonials = testimonialService.getTestimonialByPlaceId(placeId);
        List<TestimonialResponse> response = testimonials.stream()
                .map(mapper::toResponse)
                .toList();

        if(response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
