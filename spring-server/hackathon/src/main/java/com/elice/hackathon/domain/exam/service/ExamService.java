package com.elice.hackathon.domain.exam.service;


import com.elice.hackathon.domain.exam.repository.ExamRepository;
import com.elice.hackathon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    public String test(User user){
        return userRepository.findUserById(Long.valueOf(user.getUsername())).getNickName();
    }
}
