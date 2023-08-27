package com.elice.hackathon.domain.exam.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolveExamResDto {

    private String feedback;

    public static SolveExamResDto of(String feedback) {
        return SolveExamResDto.builder()
                .feedback(feedback)
                .build();
    }
}
