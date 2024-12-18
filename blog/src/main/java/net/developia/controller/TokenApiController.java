package net.developia.controller;

import lombok.RequiredArgsConstructor;
import net.developia.dto.CreateAccessTokenRequest;
import net.developia.dto.CreateAccessTokenResponse;
import net.developia.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken()); // 리프레시 토큰으로 토큰 유효성검사를 하고 , 유효한 토큰일때 리프레시토큰으로 사용자 id를 찾는다 ,사용자 id 로 사용자를 찾은 후에 토큰 제공자의 generateToken메서드를 호출해서 새로운 액세스 토큰을 생성

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

}
