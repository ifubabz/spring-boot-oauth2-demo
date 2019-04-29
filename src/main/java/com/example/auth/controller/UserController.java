package com.example.auth.controller;

import com.example.auth.account.AccountService;
import com.example.auth.oauth.CurrentUser;
import com.example.auth.oauth.SocialUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @Autowired
    AccountService accountService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public SocialUserDetails getCurrentUser(@CurrentUser SocialUserDetails socialUser) {
        log.debug("socialUser:{}", socialUser);
        String email = socialUser.getName();
        return accountService.getUserByEmail(email);
    }
}
