package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    // 원하는 컬럼만 조회
    Optional<AnswerContentAndCreateDate> findContentAndCreateDateById(Integer id);
}
