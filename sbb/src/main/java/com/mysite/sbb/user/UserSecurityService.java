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


    /*
    loadUserByUsername() 메서드는 Spring Security의 인증 과정에서 사용자 정보를 로드하기 위해 자동으로 호출됩니다. Spring Security는 로그인을 처리할 때 사용자의 아이디와 비밀번호를 확인하고, 이를 바탕으로 인증 여부를 결정합니다.
    1. 로그인 요청 처리: 사용자가 로그인 폼에 사용자 이름과 비밀번호를 입력하고 제출할 때, Spring Security는 UserDetailsService 인터페이스를 구현한 클래스 (UserSecurityService)를 사용해 사용자의 정보를 가져오려고 합니다.
        이때 loadUserByUsername() 메서드가 호출되어 데이터베이스에서 해당 사용자 정보를 가져오고, 반환된 UserDetails 객체를 사용해 인증을 진행합니다.
    2. 인증 과정: UsernamePasswordAuthenticationFilter라는 Spring Security 내부 클래스가 사용자의 인증 요청을 처리하며, 인증 과정에서 이 UserSecurityService의 loadUserByUsername() 메서드를 호출하여 사용자 정보를 조회합니다.
    3. 내부적으로 동작하는 인증 매커니즘: 사용자가 로그인할 때, Spring Security는 인증 매니저 (AuthenticationManager)를 사용하여 사용자 정보를 확인합니다.
        인증 매니저는 사용자 정보를 조회하기 위해 UserDetailsService의 loadUserByUsername()을 호출하며, 이 과정에서 반환된 UserDetails 객체의 사용자 이름과 암호화된 비밀번호를 비교하여 로그인 성공 여부를 결정합니다.
     */
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
