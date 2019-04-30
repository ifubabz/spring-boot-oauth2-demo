package com.example.auth.oauth.user;

import java.util.Map;

public class GoogleUserDetails extends SocialUserDetails {

    public GoogleUserDetails(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getSubject() {
        return this.getAttributeAsString("sub");
    }

    @Override
    public String getImageUrl() {
        if(attributes.containsKey("imageUrl")){
            return this.getAttributeAsString("imageUrl");
        }
        return this.getAttributeAsString("picture");
    }
}
