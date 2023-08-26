package com.elice.hackathon.domain.user.repository;


import com.elice.hackathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserId(String userId);

    Boolean existsByNickName(String nickName);

    User findUserByUserId(String userId);

    User findUserById(Long Id);
}
