package com.example.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class TokenAuthenticationRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("doFilterInternal:{}", request);
        String authorization = request.getHeader("Authorization");
        log.debug("doFilterInternal:Authorization:{}", authorization);
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            String bearerToken = authorization.substring(7);
            log.debug("doFilterInternal:bearerToken:{}", bearerToken);
            String email = jwtTokenService.getEmail(bearerToken);
            UserDetails userDetails = new User(email, null, Collections.singletonList(new SimpleGrantedAuthority("USER_ROLE")));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
