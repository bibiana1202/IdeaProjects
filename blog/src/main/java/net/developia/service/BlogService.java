package net.developia.service;

import lombok.RequiredArgsConstructor;
import net.developia.domain.Article;
import net.developia.dto.AddArticleRequest;
import net.developia.dto.UpdateArticleRequest;
import net.developia.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service

public class BlogService {
    private final BlogRepository blogRepository;

    // 블로그 글 작성 api
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // 블로그 글 목록 조회 api
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 블로그 글 조회 api
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // 블로그 글 삭제 api
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    // 블로그 글 수정 api
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }

}
