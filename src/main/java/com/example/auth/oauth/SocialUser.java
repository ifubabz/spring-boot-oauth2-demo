package com.example.auth.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialUser extends OAuth2User {

    SocialOAuthProvider getSocialOAuthProvider();
}
