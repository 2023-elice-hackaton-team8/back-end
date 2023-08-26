package com.elice.hackathon.domain.user.service;


import com.elice.hackathon.domain.user.dto.request.LoginReqDto;
import com.elice.hackathon.domain.user.dto.request.SignInReqDto;
import com.elice.hackathon.domain.user.dto.response.LoginResDto;
import com.elice.hackathon.domain.user.dto.response.SignInResDto;
import com.elice.hackathon.domain.user.entity.User;
import com.elice.hackathon.domain.user.repository.UserRepository;
import com.elice.hackathon.global.error.BusinessException;
import com.elice.hackathon.global.infra.redis.RedisUtil;
import com.elice.hackathon.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.elice.hackathon.global.common.ErrorMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원가입 API
    public SignInResDto signIn(SignInReqDto signInReqDto){
        if(userRepository.existsByUserId(signInReqDto.getUserId()))
            throw new BusinessException(ALREADY_SIGNUPED_Id_USER);

        if(userRepository.existsByNickName(signInReqDto.getNickName()))
            throw new BusinessException(ALREADY_SIGNUPED_NICKNAME_USER);

        String encodedPw = passwordEncoder.encode(signInReqDto.getPassword());

        User user = User.createUser(signInReqDto.getUserId(), encodedPw, signInReqDto.getNickName());

        userRepository.save(user);

        return SignInResDto.of(true);
    }

    // 로그인 API
    public LoginResDto login(LoginReqDto loginReqDto){
        User findUser = userRepository.findUserByUserId(loginReqDto.getUserId());
        if(findUser== null) throw new BusinessException(USER_NOT_FOUND);
        if(!passwordEncoder.matches(loginReqDto.getPassword(), findUser.getPassword()))
            throw new BusinessException(WRONG_PASSWORD);

        // accessToken 생성
        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", findUser.getId(), loginReqDto.getUserId()));

        return LoginResDto.of(findUser.getNickName(), accessToken);
    }
}
