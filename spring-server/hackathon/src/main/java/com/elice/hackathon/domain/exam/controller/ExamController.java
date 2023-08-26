package com.elice.hackathon.domain.exam.controller;


import com.elice.hackathon.domain.exam.dto.response.UploadImageResDto;
import com.elice.hackathon.domain.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    // 깜쥐 이미지로 풀기
    @PostMapping("/black-mouse")
    public UploadImageResDto solveBlackMouse (@RequestPart MultipartFile file) throws IOException {

        String imageUrl = examService.imageCreate(file);

        return UploadImageResDto.of(imageUrl);
    }


    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user){
        return examService.test(user);
    }
}
