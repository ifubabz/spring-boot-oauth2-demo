package com.example.auth.oauth.user;

import com.example.auth.oauth.SocialOAuthProvider;
import com.example.auth.oauth.SocialUserDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SocialUserFactory {

    public static SocialUserDetails getSocialUser(String registrationId, Map<String, Object> attributes) {
        log.debug("getSocialUser:registrationId:{}, attributes:{}", registrationId, attributes);
        SocialUserDetails userDetails = null;
        if(SocialOAuthProvider.FACEBOOK.name().equalsIgnoreCase(registrationId)){
            userDetails = new FacebookUserDetails(attributes);
        }else{
            throw new RuntimeException(String.format("Unsupported Registration ID:{}", registrationId));
        }
        return userDetails;
    }

}
