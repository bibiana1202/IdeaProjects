package com.mysite.sbb.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable=false) // 유일한 값만 저장할수 잇따 즉, 값을 중복되게 저장할수 없음
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(unique = true,nullable=false)
    private String email;

}
