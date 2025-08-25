package com.smoble.smoble.user.service;

import com.smoble.smoble.user.dto.UserResponseDto;
import com.smoble.smoble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.smoble.smoble.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto processOAuthPostLogin(OAuth2User oAuth2User, String provider) {
        String email = oAuth2User.getAttribute("email");
        String nickname = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub"); // Google 예시

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(provider, providerId, email, nickname)));

        // 필요한 경우 업데이트
        user.setNickname(nickname);
        user.setProfileImageUrl(oAuth2User.getAttribute("picture")); // Google 프로필 사진
        userRepository.save(user);

        return new UserResponseDto(user);
    }


}
