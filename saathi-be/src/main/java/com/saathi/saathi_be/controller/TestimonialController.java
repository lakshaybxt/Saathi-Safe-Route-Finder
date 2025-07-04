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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final UserService userService;
    private final TestimonialMapper mapper;

    @PostMapping //report
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

    @GetMapping(path = "/places/{placeId}")
    public ResponseEntity<List<TestimonialResponse>> getTestimonialByPlace(@PathVariable UUID placeId) {
        List<Testimonial> testimonials = testimonialService.getTestimonialByPlaceId(placeId);

        if(testimonials.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TestimonialResponse> response = testimonials.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<List<TestimonialResponse>> getMyTestimonials(@RequestAttribute UUID userId) {
        User user = userService.getUserById(userId);
        List<Testimonial> testimonials = testimonialService.getTestimonialByUser(user);

        if(testimonials.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TestimonialResponse> response = testimonials.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TestimonialResponse> updateTestimonial(
            @PathVariable UUID id,
            @RequestAttribute UUID userId,
            @Valid @RequestBody TestimonialDto testimonialDto
    ) {
        Testimonial existingTestimonial = testimonialService.getTestimonialById(id);

        if(!existingTestimonial.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Testimonial updatedTestimonial = testimonialService.updateTestimonial(id, testimonialDto);
        TestimonialResponse response = mapper.toResponse(updatedTestimonial);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTestimonial(
            @PathVariable UUID id,
            @RequestAttribute UUID userId
    ) {
        Testimonial existingTestimonial = testimonialService.getTestimonialById(id);

        if(!existingTestimonial.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }
}
