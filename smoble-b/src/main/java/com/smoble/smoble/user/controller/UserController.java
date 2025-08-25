package com.smoble.smoble.user.controller;

import com.smoble.smoble.user.dto.UserResponseDto;
import com.smoble.smoble.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/me")
    public UserResponseDto GetCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String provider = oAuth2User.getAttribute("iss");
        return userService.processOAuthPostLogin(oAuth2User, provider);
    }

}