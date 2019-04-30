package com.example.auth.oauth.user;

import java.util.Map;

public class FacebookUserDetails extends SocialUserDetails {

    public FacebookUserDetails(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getSubject() {
        return this.getAttributeAsString("id");
    }

    @Override
    public String getImageUrl() {
        if(attributes.containsKey("imageUrl")){
            return this.getAttributeAsString("imageUrl");
        }
        if(attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
}
