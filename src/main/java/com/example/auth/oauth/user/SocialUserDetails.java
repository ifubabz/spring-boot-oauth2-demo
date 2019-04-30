package com.example.auth.oauth.user;

import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Map;

@ToString
public abstract class SocialUserDetails extends User implements SocialUser {

    protected Map<String, Object> attributes;
    protected static final String NAME_ATTR_KEY = "name";

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
        return getAttributeAsString(NAME_ATTR_KEY);
    }

    protected String getAttributeAsString(String name){
        return String.valueOf(this.attributes.getOrDefault(name, ""));
    }

}
