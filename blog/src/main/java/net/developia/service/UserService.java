package net.developia.service;

import lombok.RequiredArgsConstructor;
import net.developia.domain.User;
import net.developia.dto.AddUserRequest;
import net.developia.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 유저 id 로 유저를 검색해서 전달하는 메서드
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("unexpected user"));
    }

}
