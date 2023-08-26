package com.elice.hackathon.domain.exam.entitiy;

import com.elice.hackathon.domain.user.entity.User;
import com.elice.hackathon.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

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

    @Column(name = "solve_img", nullable = true)
    private String imgUrl;
}
