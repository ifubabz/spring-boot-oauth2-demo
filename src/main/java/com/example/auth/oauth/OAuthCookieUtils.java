package com.example.auth.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Slf4j
public class OAuthCookieUtils {
    public static final String COOKIE_OAUTH2_AUTH_REQUEST = "oauth2_auth_request";
    public static final String COOKIE_REDIRECT_URI_PARAM = "redirect_uri";
    private static final int COOKIE_MAX_AGE = 180;

    public static OAuth2AuthorizationRequest getOAuth2AuthorizationRequest(HttpServletRequest request){
        log.debug("getOAuth2AuthorizationRequest:request:{}", request);
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = null;
        Cookie cookie = WebUtils.getCookie(request, COOKIE_OAUTH2_AUTH_REQUEST);
        if(cookie != null){
            Object deserializeObj = SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue()));
            oAuth2AuthorizationRequest = OAuth2AuthorizationRequest.class.cast(deserializeObj);
        }
        log.debug("getOAuth2AuthorizationRequest:oAuth2AuthorizationRequest:{}", oAuth2AuthorizationRequest);
        return oAuth2AuthorizationRequest;
    }

    public static void setOAuth2AuthorizationRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest request, HttpServletResponse response){
        log.debug("setOAuth2AuthorizationRequest:oAuth2AuthorizationRequest:{}", oAuth2AuthorizationRequest);
        if(oAuth2AuthorizationRequest!=null){
            String value = Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(oAuth2AuthorizationRequest));
            addCookie(response, COOKIE_OAUTH2_AUTH_REQUEST, value);

            String redirectUri = request.getParameter(COOKIE_REDIRECT_URI_PARAM);
            if(StringUtils.hasText(redirectUri)){
                addCookie(response, COOKIE_REDIRECT_URI_PARAM, redirectUri);
            }

        }else{
            resetCookie(request, response, COOKIE_OAUTH2_AUTH_REQUEST);
            resetCookie(request, response, COOKIE_REDIRECT_URI_PARAM);
        }
    }

    public static void removeOAuth2AuthorizationRequest(HttpServletRequest request, HttpServletResponse response){
        log.debug("removeOAuth2AuthorizationRequest:request:{}", request);
        resetCookie(request, response, COOKIE_OAUTH2_AUTH_REQUEST);
        resetCookie(request, response, COOKIE_REDIRECT_URI_PARAM);
    }

    public static String getRedirectUri(HttpServletRequest request){
        log.debug("getRedirectUri:request:{}", request);
        String redirectUri = "/";
        Cookie cookie = WebUtils.getCookie(request, COOKIE_REDIRECT_URI_PARAM);
        if(cookie!=null){
            redirectUri = cookie.getValue();
        }
        return  redirectUri;
    }

    public static void addCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    public static void resetCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
