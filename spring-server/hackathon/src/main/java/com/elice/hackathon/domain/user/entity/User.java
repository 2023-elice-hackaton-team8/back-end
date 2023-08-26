package com.elice.hackathon.domain.user.entity;

import com.elice.hackathon.domain.exam.entitiy.Exam;
import com.elice.hackathon.domain.exam.entitiy.UserExam;
import com.elice.hackathon.global.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
public class User extends BaseTimeEntity {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserExam> userExams = new ArrayList<>();

    @Builder
    public static User createUser(String userId, String password, String nickName) {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .nickName(nickName)
                .build();

        return user;
    }
}
