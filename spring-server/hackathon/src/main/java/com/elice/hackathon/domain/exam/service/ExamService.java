package com.elice.hackathon.domain.exam.service;


import com.elice.hackathon.domain.exam.repository.ExamRepository;
import com.elice.hackathon.domain.user.repository.UserRepository;
import com.elice.hackathon.global.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Transactional
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final S3Service s3Uploader;


    // 이미지 올리기
    public String imageCreate (MultipartFile file) throws IOException {
        String imgUrl = s3Uploader.upload(file);

        return imgUrl;
    }

    // test api
    public String test(User user){
        return userRepository.findUserById(Long.valueOf(user.getUsername())).getNickName();
    }
}
