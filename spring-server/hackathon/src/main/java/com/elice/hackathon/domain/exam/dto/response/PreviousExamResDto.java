package com.elice.hackathon.domain.exam.dto.response;


import com.elice.hackathon.domain.exam.entitiy.ExamType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreviousExamResDto {

    private String examExplanation;

    private String subject;

    private ExamType examType;

    private String answer;

    private String feedback;

    public static PreviousExamResDto of(String examExplanation, String subject, ExamType examType, String answer, String feedback){
        return PreviousExamResDto.builder()
                .examExplanation(examExplanation)
                .subject(subject)
                .examType(examType)
                .answer(answer)
                .feedback(feedback)
                .build();
    }

}
