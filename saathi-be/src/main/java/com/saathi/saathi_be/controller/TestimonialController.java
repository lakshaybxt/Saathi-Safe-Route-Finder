package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.TestimonialDto;
import com.saathi.saathi_be.domain.dto.response.TestimonialResponse;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.service.TestimonialService;
import com.saathi.saathi_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TestimonialResponse> reportArea(
            @Valid @RequestBody TestimonialDto testimonialDto,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);
        Testimonial testimonial = testimonialService.reportArea(testimonialDto, loggedInUser);

        TestimonialResponse response = TestimonialResponse.builder()
                .comment(testimonial.getComment())
                .tips(testimonial.getTips())
                .rating(testimonial.getRating())
                .city(testimonial.getPlace().getLocality())
                .build();
        return ResponseEntity.ok(response);
    }

}
