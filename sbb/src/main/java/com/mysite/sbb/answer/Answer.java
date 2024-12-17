package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(columnDefinition = "TEXT")
//    @Lob
    @Column(nullable = false)
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "question_id",nullable = false)  // 외래 키 컬럼 이름 명시
    private Question question;

    @ManyToOne
    private SiteUser author;

    // 수정 일시 추가하기
    private LocalDateTime modifyDate;

    // 추천 기능
    @ManyToMany
    Set<SiteUser> voter;
}
