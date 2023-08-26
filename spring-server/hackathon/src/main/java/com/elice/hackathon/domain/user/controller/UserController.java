package com.elice.hackathon.domain.user.controller;

import com.elice.hackathon.domain.user.dto.request.LoginReqDto;
import com.elice.hackathon.domain.user.dto.request.SignInReqDto;
import com.elice.hackathon.domain.user.dto.response.LoginResDto;
import com.elice.hackathon.domain.user.dto.response.SignInResDto;
import com.elice.hackathon.domain.user.service.UserService;
import com.elice.hackathon.global.common.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public BaseResponseDto<SignInResDto> signIn(@RequestBody SignInReqDto signInReqDto){
        return new BaseResponseDto<>(userService.signIn(signInReqDto));
    }

    @PostMapping("/login")
    public BaseResponseDto<LoginResDto> login(@RequestBody LoginReqDto loginReqDto){
        return new BaseResponseDto<>(userService.login(loginReqDto));
    }
}
