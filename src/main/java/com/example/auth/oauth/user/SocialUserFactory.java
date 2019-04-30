package com.example.auth.oauth.user;

import com.example.auth.account.Account;
import com.example.auth.oauth.SocialOAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SocialUserFactory {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static SocialUserDetails getSocialUser(String registrationId, Map<String, Object> attributes) {
        log.debug("getSocialUser:registrationId:{}, attributes:{}", registrationId, attributes);
        SocialUserDetails socialUserDetails = null;
        if(SocialOAuthProvider.FACEBOOK.name().equalsIgnoreCase(registrationId)){
            socialUserDetails = new FacebookUserDetails(attributes);
        }else if(SocialOAuthProvider.GOOGLE.name().equalsIgnoreCase(registrationId)){
            socialUserDetails = new GoogleUserDetails(attributes);
        }else{
            throw new RuntimeException(String.format("Unsupported Registration ID:{}", registrationId));
        }
        return socialUserDetails;
    }

    public static SocialUserDetails getSocialUser(Account account) {
        log.debug("getSocialUser:account:{}", account);
        String registrationId = account.getProvider().name();
        Map<String, Object> attributes = objectMapper.convertValue(account, Map.class);
        return getSocialUser(registrationId, attributes);
    }
}
