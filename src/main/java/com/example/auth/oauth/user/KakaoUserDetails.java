package com.example.auth.oauth.user;

import java.util.Map;

public class KakaoUserDetails extends SocialUserDetails {

    public KakaoUserDetails(Map<String, Object> attributes) {
        super(attributes, "id");
    }

    @Override
    public String getSubject() {
        return this.getAttributeAsString("id");
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return String.valueOf(properties.get("nickname"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return String.valueOf(properties.get("profile_image"));
    }
}
