package com.elice.hackathon.global.error;


import com.elice.hackathon.global.common.ErrorMessage;

public class LoginCancelException extends BusinessException{

    public LoginCancelException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
