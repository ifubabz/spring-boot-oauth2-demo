package com.example.auth.oauth.user;

import com.example.auth.oauth.SocialOAuthProvider;
import com.example.auth.oauth.SocialUser;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;

@Slf4j
public class SocialUserFactory {

    public static SocialUser getSocialUser(String registrationId, Map<String, Object> attributes) {
        log.debug("getSocialUser:registrationId:{}, attributes:{}", registrationId, attributes);
        SocialUser socialUser = null;
        if(SocialOAuthProvider.FACEBOOK.name().equalsIgnoreCase(registrationId)){
            socialUser = new FacebookUserDetails(attributes);
        }else{
            throw new RuntimeException(String.format("Unsupported Registration ID:{}", registrationId));
        }
        return socialUser;
    }
}
