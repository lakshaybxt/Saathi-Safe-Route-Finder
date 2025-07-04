package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private String username;
    private String gender;
    private String state;
}
