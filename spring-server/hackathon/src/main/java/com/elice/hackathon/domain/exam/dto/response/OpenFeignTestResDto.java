package com.elice.hackathon.domain.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenFeignTestResDto {
    private Long code;

    private Boolean isSuccess;

    private String message;

    private TestResult result;

    @Getter
    static class TestResult{
        private Long userId;
        private String name;
        private String accessToken;
        private String refreshToken;
    }
}

