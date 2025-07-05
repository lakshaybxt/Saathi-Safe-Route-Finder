package com.saathi.saathi_be.domain.dto.response;

import com.saathi.saathi_be.domain.entity.EmergencyContact;
import com.saathi.saathi_be.domain.entity.Testimonial;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserResponse {
    private UUID id;
    private String username;
    private String email;
    private String gender;
    private String state;
    private long totalEmergencyContacts;
    private long totalTestimonials;
}
