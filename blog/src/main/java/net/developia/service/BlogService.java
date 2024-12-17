package net.developia.service;

import lombok.RequiredArgsConstructor;
import net.developia.domain.Article;
import net.developia.dto.AddArticleRequest;
import net.developia.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}
