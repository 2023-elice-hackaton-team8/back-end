package com.elice.hackathon.domain.exam.controller;


import com.elice.hackathon.domain.exam.dto.request.SolveBlackReqDto;
import com.elice.hackathon.domain.exam.dto.request.SolveWhiteReqDto;
import com.elice.hackathon.domain.exam.dto.response.GetProblemResDto;
import com.elice.hackathon.domain.exam.dto.response.GetSubTitleResDto;
import com.elice.hackathon.domain.exam.dto.response.PreviousExamResDto;
import com.elice.hackathon.domain.exam.dto.response.SolveExamResDto;
import com.elice.hackathon.domain.exam.service.ExamService;
import com.elice.hackathon.global.common.dto.BaseResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.elice.hackathon.domain.exam.entitiy.Exam.examSubTypes;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    @PostConstruct
    public void init() {
        examSubTypes.put( "사회문화", new String[]{"사회 문화 현상의 탐구", "개인과 사회 구조", "문화와 일상생활", "사회 계층과 불평등", "현대 사회 변동"});
        examSubTypes.put( "사회 문화 현상의 탐구", new String[]{"사회 문화 현상의 이해", "사회 문화 현상의 연구 방법", "자료 수집 방법", "사회 문화 현상의 탐구 태도와 연구 윤리"});
        examSubTypes.put( "개인과 사회 구조", new String[]{"사회적 존재로서의 인간", "사회 집단과 사회 조직", "사회 구조와 일탈 행동"});
        examSubTypes.put( "문화와 일상생활", new String[]{"문화의 이해", "현대 사회의 문화 양상", "문화 변동의 양상과 대응"});
        examSubTypes.put( "사회 계층과 불평등", new String[]{"사회 불평등 현상의 이해", "사회 이동과 사회 계층 구조", "다양한 사회 불평등 현상", "사회 복지와 보기 제도"});
        examSubTypes.put( "현대 사회 변동", new String[]{"사회 변동과 사회 운동", "현대 사회의 변화와 전 지구적 수준의 문제"});
    }

    // 깜쥐 이미지로 풀기
    @PostMapping("/black-mouse")
    public BaseResponseDto<SolveExamResDto> solveBlackMouse(@AuthenticationPrincipal User user, @RequestPart MultipartFile file, @RequestParam String examId) throws IOException {
        return new BaseResponseDto<>(examService.solveBlackMouseService(user, file, examId));
    }

    // 백쥐 이미지로 풀기
    @PostMapping("/white-mouse")
    public BaseResponseDto<SolveExamResDto> solveBlackMouse(@AuthenticationPrincipal User user, @RequestBody SolveWhiteReqDto solveWhiteReqDto) {
        return new BaseResponseDto<>(examService.solveWhiteMouseService(user, solveWhiteReqDto));
    }

    @GetMapping("/previous")
    public BaseResponseDto<List<PreviousExamResDto>> previousExams(@AuthenticationPrincipal User user){
        return new BaseResponseDto<>(examService.previousExams(user));
    }

    @GetMapping("")
    public BaseResponseDto<GetProblemResDto> getProblem(@RequestParam String subjectMid, @RequestParam String type) {
        return new BaseResponseDto<>(examService.selectRandomProblem(subjectMid, type));
    }


    // 과목, 대단원 선택시 하위 타입들 보여주기
    @GetMapping("/sub-title")
    public BaseResponseDto<GetSubTitleResDto> getSubTitle(@RequestParam String title){
        return new BaseResponseDto<>(examService.getSubTitle(title));
    }


//    @GetMapping("/test")
//    public String test(@AuthenticationPrincipal User user){
//        return examService.test(user);
//    }

}
