package com.elice.hackathon.domain.exam.dto.response;


import com.elice.hackathon.domain.exam.entitiy.ExamType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreviousExamResDto {

    private Long examId;

    private String examExplanation;

    private String subject;

    private ExamType examType;

    public static PreviousExamResDto of(Long examId, String examExplanation, String subject, ExamType examType){
        return PreviousExamResDto.builder()
                .examId(examId)
                .examExplanation(examExplanation)
                .subject(subject)
                .examType(examType)
                .build();
    }

}
