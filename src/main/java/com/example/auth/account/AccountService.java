package com.example.auth.account;

import com.example.auth.oauth.user.SocialUserDetails;
import com.example.auth.oauth.user.SocialUserFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadUserByUsername:username:{}", username);
        Optional<Account> accountOptional = accountRepository.findByEmail(username);
        SocialUserDetails userDetails = null;
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            log.debug("loadUserByUsername:Account:{}", account);
            userDetails = SocialUserFactory.getSocialUser(account);
        }else{
            throw new RuntimeException("User Not Found.");
        }
        log.debug("loadUserByUsername:UserDetails:{}", userDetails);
        return userDetails;
    }

    public AccountDto.Response getUserByEmail(String email) {
        log.debug("getUserByEmail:email:{}", email);
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        AccountDto.Response accountDto = null;
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            log.debug("getUserByEmail:Account:{}", account);
            accountDto = modelMapper.map(account, AccountDto.Response.class);
        }
        log.debug("getUserByEmail:Response:{}", accountDto);
        return accountDto;
    }
}
