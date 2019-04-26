package com.example.auth.security;

import com.example.auth.oauth.SocialUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenService {

    private final String JWT_SET_URL = "https://example.com/oauth2/keys";
    private JwtDecoder jwtDecoder = new NimbusJwtDecoderJwkSupport(JWT_SET_URL);

    public String createToken(Authentication authentication) {
        log.debug("createToken:{}", authentication);
        String token = null;
        SocialUserDetails socialUserDetails = (SocialUserDetails)authentication.getPrincipal();
        return token;
    }

    public String getEmail(String token){
        log.debug("getEmail:{}", token);
        return getValue(token, "email");
    }

    private String getValue(String token, String key){
        Jwt jwt = jwtDecoder.decode(token);
        return String.valueOf(jwt.getClaims().get(key));
    }
}
