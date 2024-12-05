package com.mysite.sbb.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
// 스프링 시큐리티가 로그인시 사용할 서비스
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;


    //
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 찾기
        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);

        // 사용자가 존재하지 않는 경우
        if(_siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        // 사용자 정보 가져오기
        SiteUser siteUser = _siteUser.get();

        // 권한 설정
        // GrantedAuthority 인터페이스는 사용자의 권한(역할)을 나타내며, 이를 구현한 클래스가 SimpleGrantedAuthority입니다.
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        // UserDetails 객체 반환
        // User 클래스는 Spring Security에서 제공하는 UserDetails 인터페이스의 기본 구현체
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
