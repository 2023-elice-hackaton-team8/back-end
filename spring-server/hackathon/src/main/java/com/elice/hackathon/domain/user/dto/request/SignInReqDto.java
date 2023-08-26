package com.elice.hackathon.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignInReqDto {
    private String userId;

    private String nickName;

    private String password;
}
