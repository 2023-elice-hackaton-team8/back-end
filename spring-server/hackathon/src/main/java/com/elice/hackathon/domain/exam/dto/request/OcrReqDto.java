package com.elice.hackathon.domain.exam.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OcrReqDto {
    private String uri;

    public static OcrReqDto of(String uri){
        return OcrReqDto.builder()
                .uri(uri)
                .build();
    }
}
