package com.example.auth.oauth;

import com.example.auth.account.Account;
import com.example.auth.account.AccountRepository;
import com.example.auth.oauth.user.SocialUserDetails;
import com.example.auth.oauth.user.SocialUserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SocialOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("loadUser:{}", userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialOAuthProvider provider = SocialOAuthProvider.valueOf(registrationId.toUpperCase(Locale.ENGLISH));
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.debug("loadUser:attributes:{}", attributes);
        SocialUserDetails socialUserDetails = SocialUserFactory.getSocialUser(registrationId, attributes);
        log.debug("loadUser:socialUserDetails:{}", socialUserDetails);
        Optional<Account> accountOptional = accountRepository.findByEmail(socialUserDetails.getEmail());
        log.debug("loadUser:accountOptional:{}", accountOptional);
        Account account = null;
        if(accountOptional.isPresent()){
            account = accountOptional.get();
            account.setName(socialUserDetails.getUsername());
            account.setProvider(provider);
            account.setImageUrl(socialUserDetails.getImageUrl());
            account.setSubject(socialUserDetails.getSubject());
        }else{
            account = Account.builder()
                    .name(socialUserDetails.getUsername())
                    .email(socialUserDetails.getEmail())
                    .provider(provider)
                    .subject(socialUserDetails.getSubject())
                    .imageUrl(socialUserDetails.getImageUrl())
                    .build();
        }
        log.debug("loadUser:Account:{}", account);
        accountRepository.save(account);
        log.debug("loadUser:socialUser:{}", socialUserDetails);
        return socialUserDetails;
    }
}
