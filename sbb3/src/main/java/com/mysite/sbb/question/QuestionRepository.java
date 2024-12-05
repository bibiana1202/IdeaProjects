package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    // 원하는 컬럼만 조회
    Optional<QuestionSubjectAndContent> findSubAndConById(Integer id);
    // 페이징 처리
    Page<Question> findAll(Pageable pageable);
}
