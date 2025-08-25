package com.smoble.smoble.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 (소셜 로그인 식별용)
    @Column(unique = true, nullable = false)
    private String email;

    // 화면에 표시할 닉네임
    @Column(nullable = false)
    private String nickname;

    // 프로필 이미지 URL
    private String profileImageUrl;

    // 소셜 제공자 (google, kakao, naver)
    @Column(nullable = false)
    private String provider;

    // 소셜에서 제공하는 고유 ID (예: 구글 sub, 카카오 id)
    @Column(nullable = false)
    private String providerId;

    // 권한 (기본: ROLE_USER)
    private String role = "ROLE_USER";

    // 자기소개
    private String bio;

    private Long temperature_score;

    // 생성/수정 시간
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    public User(String provider, String providerId, String email, String nickname) {
        this.provider = provider;       // 소셜 제공자
        this.providerId = providerId;   // 소셜 고유 ID
        this.email = email;             // 이메일
        this.nickname = nickname;       // 닉네임
        this.role = "ROLE_USER";        // 기본 권한
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}