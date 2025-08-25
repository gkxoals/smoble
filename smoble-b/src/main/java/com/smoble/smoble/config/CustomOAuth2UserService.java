package com.smoble.smoble.config;

import com.smoble.smoble.user.entity.User;
import com.smoble.smoble.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao
        String providerId;
        String email;
        String nickname;
        String profileImageUrl = null;

        // 소셜 제공자별 정보 처리
        if ("google".equals(registrationId)) {
            providerId = oAuth2User.getAttribute("sub");
            email = oAuth2User.getAttribute("email");
            nickname = oAuth2User.getAttribute("name");
            profileImageUrl = oAuth2User.getAttribute("picture");
        } else if ("kakao".equals(registrationId)) {
            providerId = String.valueOf(oAuth2User.getAttribute("id"));

            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            email = (String) kakaoAccount.get("email");

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            nickname = (String) profile.get("nickname");
            profileImageUrl = (String) profile.get("profile_image_url");
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("이메일 정보가 존재하지 않습니다.");
        }

        // DB 조회 후 저장 또는 업데이트
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(registrationId, providerId, email, nickname)));

        // 기존 유저라면 필요 시 프로필 업데이트
        boolean needUpdate = false;

        if (nickname != null && !nickname.equals(user.getNickname())) {
            user.setNickname(nickname);
            needUpdate = true;
        }

        if (profileImageUrl != null && !profileImageUrl.equals(user.getProfileImageUrl())) {
            user.setProfileImageUrl(profileImageUrl);
            needUpdate = true;
        }

        if (needUpdate) {
            userRepository.save(user);
        }

        // OAuth2User 반환
        return oAuth2User;
    }
}
