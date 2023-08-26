package com.elice.hackathon.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResDto {

    private String nickName;

    private String accessToken;

    public static LoginResDto of(String nickName, String accessToken){
        return LoginResDto.builder()
                .nickName(nickName)
                .accessToken(accessToken)
                .build();
    }
}
