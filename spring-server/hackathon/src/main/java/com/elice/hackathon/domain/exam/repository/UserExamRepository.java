package com.elice.hackathon.domain.exam.repository;

import com.elice.hackathon.domain.exam.entitiy.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExamRepository extends JpaRepository<UserExam, Long> {
}
