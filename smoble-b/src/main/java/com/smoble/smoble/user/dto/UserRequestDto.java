package com.smoble.smoble.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRequestDto {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private String role;
    private Integer temperatureScore;
    private String bio;

}