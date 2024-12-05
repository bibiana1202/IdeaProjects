package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser,Long> {

    // 사용자 id를 조회하는 기능, 사용자 id로 SiteUser 엔티티를 조회하는 findByusername 메서드
    Optional<SiteUser> findByUsername(String username);
}
