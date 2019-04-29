package com.example.auth.oauth;

import com.example.auth.account.Account;
import com.example.auth.account.AccountRepository;
import com.example.auth.oauth.user.SocialUserFactory;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

        SocialUserDetails socialUserDetails = SocialUserFactory.getSocialUser(registrationId, attributes);
        Optional<Account> accountOptional = accountRepository.findByEmail(socialUserDetails.getUsername());

        Account account = null;
        if(accountOptional.isPresent()){
            account = accountOptional.get();
            account.setName(socialUserDetails.getName());
            account.setProvider(provider);
        }else{
            account = new Account();
            account.setName(socialUserDetails.getName());
            account.setEmail(socialUserDetails.getName());
            account.setProvider(provider);
        }
        accountRepository.save(account);

        log.debug("socialUser:{}", socialUserDetails);
        return socialUserDetails;
    }
}
