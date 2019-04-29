package com.example.auth.oauth;

import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

@ToString
public abstract class SocialUserDetails extends User implements OAuth2User {

    protected Map<String, Object> attributes;
    protected static final String NAME_ATTR_KEY = "email";

    protected SocialUserDetails(Map<String, Object> attributes){
        this(attributes, NAME_ATTR_KEY);
    }

    protected SocialUserDetails(Map<String, Object> attributes, String nameKey) {
        super(String.valueOf(attributes.get(nameKey)), String.valueOf(attributes.get(nameKey)),  Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return getAttribute(NAME_ATTR_KEY);
    }

    protected String getAttribute(String name){
        return String.valueOf(this.attributes.get(name));
    }

}
