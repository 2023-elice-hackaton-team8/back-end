package com.elice.hackathon.domain.exam.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlackMouseResDto {
    Long examId;

    String explanation;

    public static BlackMouseResDto of(Long examId, String explanation){
        return BlackMouseResDto.builder()
                .examId(examId)
                .explanation(explanation)
                .build();
    }
}
