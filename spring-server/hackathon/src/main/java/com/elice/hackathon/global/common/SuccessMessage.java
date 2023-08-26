package com.elice.hackathon.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
public enum SuccessMessage {
    SUCCESS(OK, true, "요청에 성공하였습니다.");

    private final int code;
    private final boolean isSuccess;
    private final String message;

    SuccessMessage(HttpStatus code, boolean isSuccess, String message) {
        this.code = code.value();
        this.isSuccess = isSuccess;
        this.message = message;
    }

}
