package com.example.auth.oauth.user;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialUser extends OAuth2User {

    String getSubject();

    String getEmail();

    String getImageUrl();

}
