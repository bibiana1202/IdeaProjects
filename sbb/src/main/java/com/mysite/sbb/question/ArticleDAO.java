package com.mysite.sbb.question;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ArticleDAO {
    void insertArticle(ArticleDTO articleDTO) throws Exception;

    List<ArticleDTO> getArticleList() throws Exception;

    ArticleDTO getDetail(long no) throws Exception;

    int deleteArticle(ArticleDTO articleDTO) throws Exception;

    int updateArticle(ArticleDTO articleDTO) throws Exception;

}