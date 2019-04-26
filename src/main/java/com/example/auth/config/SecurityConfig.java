package com.example.auth.config;

import com.example.auth.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.auth.oauth.OAuth2AuthenticationFailureHandler;
import com.example.auth.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.auth.oauth.SocialOAuth2UserService;
import com.example.auth.security.RequestAuthenticationEntryPoint;
import com.example.auth.security.TokenAuthenticationRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SocialOAuth2UserService socialOAuth2UserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
            .csrf()
                .disable()
            .httpBasic()
                .disable()
            .formLogin()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/", "login/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
            .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/login/oauth")
                    .authorizationRequestRepository(authorizationRequestRepository())
                    .and()
                .redirectionEndpoint()
                    .baseUri("/login/callback/*")
                    .and()
                .userInfoEndpoint()
                    .userService(socialOAuth2UserService)
                    .and()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
            ;
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new RequestAuthenticationEntryPoint();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository(){
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler(){
        return new OAuth2AuthenticationSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler(){
        return new OAuth2AuthenticationFailureHandler();
    }

    @Bean
    public Filter tokenAuthenticationFilter(){
        return new TokenAuthenticationRequestFilter();
    }
}
