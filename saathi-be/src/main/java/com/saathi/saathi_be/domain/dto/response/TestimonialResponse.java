package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestimonialResponse {
    private String id;
    private String comment;
    private int rating;
    private String tips;
    private String city;
}
