package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SosResponseDto {
    private String message;
    private LocalDateTime timeStamp;
}
