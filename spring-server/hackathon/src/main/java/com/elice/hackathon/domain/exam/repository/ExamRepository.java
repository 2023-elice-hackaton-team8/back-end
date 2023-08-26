package com.elice.hackathon.domain.exam.repository;

import com.elice.hackathon.domain.exam.entitiy.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}
