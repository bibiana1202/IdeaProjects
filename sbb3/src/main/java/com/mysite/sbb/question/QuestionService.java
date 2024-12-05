package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
// Lombok 라이브러리에서 제공하는 어노테이션으로, 클래스의 final 필드나 @NonNull 필드에 대해 생성자를 자동으로 생성
@Service
public class QuestionService {

    // questionRepository 객체는 RequiredArgsConstructor에 의해 생성자 방식으로 주입된다
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page){
        // 최신순으로 데이터 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        // 페이징 처리
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts)); // 최신순으로 데이터 조회
        return this.questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        }
        else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content){
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }
}
