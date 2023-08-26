package com.elice.hackathon.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {
    String accessToken;

    public static TokenDto of(String accessToken, String refreshToken){
        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
