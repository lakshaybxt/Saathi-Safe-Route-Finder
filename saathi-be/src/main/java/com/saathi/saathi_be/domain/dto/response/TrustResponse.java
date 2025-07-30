package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrustResponse {
    private long reviewers;
    private long cities;
}
