package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<Question> getList(int page,String kw){
        // 최신순으로 데이터 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        // 페이징 처리
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts)); // 최신순으로 데이터 조회
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
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

    public void create(String subject, String content, SiteUser user){
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question qeustion){
        this.questionRepository.delete(qeustion);
    }

    // 추천인 저장
    public void vote(Question question,SiteUser siteUser){
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    // 검색기능
    private Specification<Question> search(String kw){
        return new Specification<>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate (Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb){
                query.distinct(true); // 중복을 제거
                Join<Question,SiteUser> u1 = q.join("author" ,JoinType.LEFT); // 질문 작성자
                Join<Question,Answer> a = q.join("answerList",JoinType.LEFT); // 답변
                Join<Answer,SiteUser> u2 = a.join("author",JoinType.LEFT);    // 답변 작성자

                // DBMS_LOB.SUBSTR 함수 사용
                Expression<String> questionContentSubstring = cb.function("DBMS_LOB.SUBSTR", String.class, q.get("content"), cb.literal(4000), cb.literal(1));
                Expression<String> answerContentSubstring = cb.function("DBMS_LOB.SUBSTR", String.class, a.get("content"), cb.literal(4000), cb.literal(1));

                return  cb.or(
                        cb.like(q.get("subject"), "%" + kw + "%"),            // 제목
//                        cb.like(questionContentSubstring, "%" + kw + "%"),    // 질문 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),          // 질문 작성자
//                        cb.like(answerContentSubstring, "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%")           // 답변 작성자

                );
            }
        };
    }
}
