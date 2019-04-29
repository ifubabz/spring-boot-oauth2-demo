package com.example.auth.config;

import com.example.auth.account.AccountService;
import com.example.auth.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.auth.oauth.OAuth2AuthenticationFailureHandler;
import com.example.auth.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.auth.oauth.SocialOAuth2UserService;
import com.example.auth.security.RequestAuthenticationEntryPoint;
import com.example.auth.security.TokenAuthenticationRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SocialOAuth2UserService socialOAuth2UserService;

    @Autowired
    AccountService accountService;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(accountService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/login/**")
                .permitAll()
                .anyRequest()
                .authenticated()
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
                .failureHandler(authenticationFailureHandler());

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.cors()
//                .and()
//            .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//            .csrf()
//                .disable()
//            .httpBasic()
//                .disable()
//            .formLogin()
//                .disable()
//            .authorizeRequests()
//                .antMatchers("/", "login/**")
//                    .permitAll()
//                .anyRequest()
//                    .authenticated()
//                .and()
//            .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint())
//                .and()
//            .oauth2Login()
//                .authorizationEndpoint()
//                    .baseUri("/login/oauth")
//                    .authorizationRequestRepository(authorizationRequestRepository())
//                    .and()
//                .redirectionEndpoint()
//                    .baseUri("/login/callback/*")
//                    .and()
//                .userInfoEndpoint()
//                    .userService(socialOAuth2UserService)
//                    .and()
//                .successHandler(authenticationSuccessHandler())
//                .failureHandler(authenticationFailureHandler())
//            ;
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
