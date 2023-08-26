package com.elice.hackathon.domain.user.dto.request;

import lombok.Getter;

@Getter
public class LoginReqDto {
    private String userId;

    private String password;
}
