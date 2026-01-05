package com.tripmate.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 여행 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCreateRequest {

    @NotBlank(message = "여행 제목은 필수입니다")
    private String title;

    @NotBlank(message = "여행지는 필수입니다")
    private String destination;

    @NotNull(message = "시작일은 필수입니다")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다")
    private LocalDate endDate;

    private String description;

    private Double budget;

    private Boolean isPublic;
}