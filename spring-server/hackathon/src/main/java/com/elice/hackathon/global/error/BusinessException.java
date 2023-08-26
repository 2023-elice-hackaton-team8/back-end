package com.elice.hackathon.global.error;

import com.elice.hackathon.global.common.ErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}

