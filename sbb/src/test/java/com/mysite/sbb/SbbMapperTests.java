package com.mysite.sbb;

import com.mysite.sbb.question.ArticleDAO;
import com.mysite.sbb.question.ArticleDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@SpringBootTest
class SbbMapperTests {

    @Autowired
    private ArticleDAO articleDAO;

    @Test
    @Transactional
    void testFindAll() throws Exception {
        List<ArticleDTO> list = articleDAO.getArticleList();
        for(ArticleDTO dto:list) {
            log.info(dto);
        }
    }
}