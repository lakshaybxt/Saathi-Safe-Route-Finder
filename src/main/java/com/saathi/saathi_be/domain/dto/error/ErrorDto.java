package com.saathi.saathi_be.domain.dto.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private Integer status;
    private String message;
}
