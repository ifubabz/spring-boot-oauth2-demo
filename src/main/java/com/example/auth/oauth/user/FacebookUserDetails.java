package com.example.auth.oauth.user;

import com.example.auth.oauth.SocialOAuthProvider;
import com.example.auth.oauth.SocialUserDetails;

import java.util.Map;

public class FacebookUserDetails extends SocialUserDetails {

    public FacebookUserDetails(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public SocialOAuthProvider getSocialOAuthProvider() {
        return SocialOAuthProvider.FACEBOOK;
    }
}
