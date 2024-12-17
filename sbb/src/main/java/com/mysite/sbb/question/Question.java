package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String subject;

    //@Column(columnDefinition = "TEXT")
//    @Lob // 오라클 clob 타입
    @Column(nullable = false)
    private String content;

    private LocalDateTime createDate;

    // 참조 : 하나의 Question 객체가 여러개의 Answer 객체를 가질수 있다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    // 질문 엔티티에 속성 추가하기
    @ManyToOne
    private SiteUser author;

    // 수정 일시 추가
    private LocalDateTime modifyDate;

    // 추천기능
    @ManyToMany
    Set<SiteUser> voter;
}
