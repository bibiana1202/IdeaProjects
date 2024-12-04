package com.mysite.sbb.answer;

import java.time.LocalDateTime;

// jpa 프로젝션(투사)
public interface AnswerContentAndCreateDate {
    String getContent();
    LocalDateTime getCreateDate();
}
