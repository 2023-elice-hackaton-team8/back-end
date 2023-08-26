package com.elice.hackathon.domain.exam.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Exam {

    @Id @Column(name = "exam_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "answer")
    private String answer;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private ExamType type;

    @Column(name = "subject")
    private String subject;

    @Column(name = "subject_big")
    private String subjectBig;

    @Column(name = "subject_mid")
    private String subjectMid;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
    private List<UserExam> userExams = new ArrayList<>();

    public static Exam createExam(String explanation, String answer, ExamType type, String subject, String subjectBig, String subjectMid) {
        Exam exam = Exam.builder()
                .explanation(explanation)
                .answer(answer)
                .type(type)
                .subject(subject)
                .subjectBig(subjectBig)
                .subjectMid(subjectMid)
                .build();
        return exam;
    }

}
