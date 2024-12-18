package net.developia.service;

import lombok.RequiredArgsConstructor;
import net.developia.domain.RefreshToken;
import net.developia.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    // 전달받은 리프레시 토큰으로 토큰 객체를 검색해서 전달하는 메서드
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new IllegalArgumentException("Invalid refresh token"));
    }
}