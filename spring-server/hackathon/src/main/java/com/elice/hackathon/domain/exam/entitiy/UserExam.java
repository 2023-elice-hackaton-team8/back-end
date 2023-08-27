package com.elice.hackathon.domain.exam.entitiy;

import com.elice.hackathon.domain.user.entity.User;
import com.elice.hackathon.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Entity
@Getter
public class UserExam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "user_answer")
    private String userAnswer;

    public static UserExam createUserExam(User user, Exam exam, String userAnswer, String feedback) {
        UserExam userExam = UserExam.builder()
                .user(user)
                .exam(exam)
                .userAnswer(userAnswer)
                .feedback(feedback)
                .build();
        return userExam;
    }
}
