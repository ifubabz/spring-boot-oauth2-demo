package com.example.auth.oauth;

import com.example.auth.security.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("onAuthenticationSuccess:request:{}, authentication:{}", request, authentication);
        String redirectUri = OAuthCookieUtils.getRedirectUri(request);
        String token = jwtTokenService.createToken(authentication);
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build()
                .toUriString();
        log.debug("onAuthenticationSuccess:targetUrl:{}", targetUrl);
        if (response.isCommitted()) {
            log.debug("Response has been committed. Unable to redirect:{}", targetUrl);
            return;
        }

        super.clearAuthenticationAttributes(request);
        OAuthCookieUtils.removeOAuth2AuthorizationRequest(request, response);
        log.debug("onAuthenticationSuccess:request:{}", request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
