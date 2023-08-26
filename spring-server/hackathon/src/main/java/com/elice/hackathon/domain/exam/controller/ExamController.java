package com.elice.hackathon.domain.exam.controller;


import com.elice.hackathon.domain.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user){
        return examService.test(user);
    }
}
