package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경 설정 파일
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션 , 스프링 시큐리티를 활성화 하는 역할
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true,jsr250Enabled = true) //QuestionController와 AnswerController에서 로그인 여부를 판별할 때 사용한 PreAuthorize 애너테이션을 사용하기 위해 반드시 필요한 설정
public class SecurityConfig {
    // 내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어 URL별로 특별한 설정을 할수 있게 된다.
    // 스프링 시큐리티 세부 설정은 Bean 애너테이션을 통해 SecurityFilterChain 빈을 생성하여 설정할수 있다.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 인증 되지 않은 모든 페이지의 요청을 허락
                .csrf((csrf)->csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))) // /h2-console/로 시작하는 모든 URL은 CSRF 검증을 하지 않는다
                .headers((headers) -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) //URL 요청 시 X-Frame-Options 헤더를 DENY 대신 SAMEORIGIN으로 설정하여 오류가 발생하지 않도록 함.
                .formLogin(formLogin -> formLogin.loginPage("/user/login").defaultSuccessUrl("/")) //  로그인 페이지의 URL은 /user/login이고 로그인 성공 시에 이동할 페이지는 루트 URL(/)임을 의미
                .logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessUrl("/").invalidateHttpSession(true)) //로그아웃 URL을 /user/logout으로 설정하고 로그아웃이 성공하면 루트(/) 페이지로 이동하도록 했다. 그리고 .invalidateHttpSession(true)를 통해 로그아웃 시 생성된 사용자 세션도 삭제하도록 처리
                ;
        return http.build();
    }

    // BCryptPasswordEncoder 객체를 직접 new 로 생성하는 방식 보다는 PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다.
    // PasswordEncoder는 BCryptPasswordEncoder의 인터페이스이다.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티의 인증을 처리
    // AuthenticationManager 빈을 생성
    // AuthenticationManager는 Spring Security의 핵심 인터페이스로, 사용자 인증을 담당 -> 실제로 인증 요청이 들어오면 사용자 정보를 검사하고, 그에 따라 인증 여부를 결정
    // 사용자 인증 시 앞에서 작성한 UserSecurityService와 PasswordEncoder를 내부적으로 사용하여 인증과 권한 부여 프로세스를 처리
    // AuthenticationConfiguration은 Spring Security에서 제공하는 클래스 중 하나로, 기본적인 인증 설정을 제공
    // 이 객체를 사용하면 Spring Security에 등록된 인증 설정에 접근할 수 있고, 인증 처리에 필요한 AuthenticationManager를 얻어올 수 있습니다.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
