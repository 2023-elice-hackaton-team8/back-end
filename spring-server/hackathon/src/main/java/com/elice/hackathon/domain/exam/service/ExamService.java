package com.elice.hackathon.domain.exam.service;


import com.elice.hackathon.domain.exam.dto.request.ProblemReqDto;
import com.elice.hackathon.domain.exam.dto.response.BlackMouseResDto;
import com.elice.hackathon.domain.exam.dto.response.GetSubTitleResDto;
import com.elice.hackathon.domain.exam.entitiy.Exam;
import com.elice.hackathon.domain.exam.entitiy.ExamType;
import com.elice.hackathon.domain.exam.repository.ExamRepository;
import com.elice.hackathon.domain.user.repository.UserRepository;
import com.elice.hackathon.global.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final S3Service s3Uploader;


    // 이미지 올리기
    public String imageCreate(MultipartFile file) throws IOException {
        String imgUrl = s3Uploader.upload(file);

        return imgUrl;
    }

    // 깜쥐 서비스
    public BlackMouseResDto blackMouseService(MultipartFile file, ProblemReqDto problemReqDto) throws IOException {
        String imgUrl = imageCreate(file);

        Exam selectedExam = selectRandomProblem(problemReqDto);

        return BlackMouseResDto.of(selectedExam.getId(), selectedExam.getExplanation());
    }

    // 문제 type과 중단원 받아서 랜덤 문제 뽑아주는 APi
    private Exam selectRandomProblem(ProblemReqDto problemReqDto){
        List<Exam> exams = examRepository.findAllByTypeEqualsAndSubjectMidEquals(ExamType.valueOf(problemReqDto.getType()), problemReqDto.getSubjectMid());
        Random rand = new Random();
        int random = rand.nextInt(exams.size());

        return exams.get(random);
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
