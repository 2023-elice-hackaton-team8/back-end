package com.elice.hackathon.domain.exam.repository;

import com.elice.hackathon.domain.exam.entitiy.Exam;
import com.elice.hackathon.domain.exam.entitiy.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findAllByTypeEqualsAndSubjectMidEquals(ExamType type, String subjectMid);
}
