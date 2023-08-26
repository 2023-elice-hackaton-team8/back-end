package com.elice.hackathon.domain.exam.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetProblemResDto {
    Long examId;

    String explanation;

    public static GetProblemResDto of(Long examId, String explanation){
        return GetProblemResDto.builder()
                .examId(examId)
                .explanation(explanation)
                .build();
    }
}
