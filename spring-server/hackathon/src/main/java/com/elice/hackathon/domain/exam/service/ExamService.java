package com.elice.hackathon.domain.exam.service;


import com.elice.hackathon.domain.exam.dto.request.OcrReqDto;
import com.elice.hackathon.domain.exam.dto.request.ProblemReqDto;
import com.elice.hackathon.domain.exam.dto.request.SolveReqDto;
import com.elice.hackathon.domain.exam.dto.response.GetProblemResDto;
import com.elice.hackathon.domain.exam.dto.response.GetSubTitleResDto;
import com.elice.hackathon.domain.exam.entitiy.Exam;
import com.elice.hackathon.domain.exam.entitiy.ExamType;
import com.elice.hackathon.domain.exam.feign.OCROpenFeign;
import com.elice.hackathon.domain.exam.repository.ExamRepository;
import com.elice.hackathon.domain.user.repository.UserRepository;
import com.elice.hackathon.global.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final OCROpenFeign ocrOpenFeign;
    private final S3Service s3Uploader;


    // 이미지 올리기
    public String imageCreate(MultipartFile file) throws IOException {
        String imgUrl = s3Uploader.upload(file);

        return imgUrl;
    }

    // 깜쥐 서비스
    public GetProblemResDto solveBlackMouseService(User user, MultipartFile file, SolveReqDto solveReqDto) throws IOException {
        String imgUrl = imageCreate(file);

        // ocr 불러서 text 받고
        String extarctString = ocrOpenFeign.ocrFeign(OcrReqDto.of(imgUrl));

        // 모델 불러서 채점 받기

        String userId = userRepository.findUserById(Long.valueOf(user.getUsername())).getUserId();

        return GetProblemResDto.of(1L, "1");
    }

    // 문제 type과 중단원 받아서 랜덤 문제 뽑아주는 APi
    public GetProblemResDto selectRandomProblem(String subjectMid, String type){
        List<Exam> exams = examRepository.findAllByTypeEqualsAndSubjectMidEquals(ExamType.valueOf(type), subjectMid);
        Random rand = new Random();
        int random = rand.nextInt(exams.size());

        return GetProblemResDto.of(exams.get(random).getId(), exams.get(random).getExplanation());
    }

    // 문제 하위 타이틀 뽑아주는 API
    public GetSubTitleResDto getSubTitle(String title){
        if(!Exam.examSubTypes.containsKey(title))
            return GetSubTitleResDto.of(new String[]{});

        String[] subTitles = Exam.examSubTypes.get(title);

        return GetSubTitleResDto.of(subTitles);
    }


    // test api
    public String test(User user){
        return userRepository.findUserById(Long.valueOf(user.getUsername())).getNickName();
    }
}
