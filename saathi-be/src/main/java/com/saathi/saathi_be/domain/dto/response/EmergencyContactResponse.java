package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmergencyContactResponse {
    private UUID id;
    private String name;
    private String phoneNo;
    private String relation;
    private UUID userId;
}
