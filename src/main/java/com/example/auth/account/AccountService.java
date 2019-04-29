package com.example.auth.account;

import com.example.auth.oauth.SocialUserDetails;
import com.example.auth.oauth.user.SocialUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByEmail(username);
        UserDetails userDetails = null;
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", account.getEmail());
            userDetails = SocialUserFactory.getSocialUser(account.getProvider().name(), attributes);
        }
        return userDetails;
    }

    public SocialUserDetails getUserByEmail(String email) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        SocialUserDetails userDetails = null;
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", account.getEmail());
            userDetails = SocialUserFactory.getSocialUser(account.getProvider().name(), attributes);
        }
        return userDetails;
    }
}
