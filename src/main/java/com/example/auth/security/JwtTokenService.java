package com.example.auth.security;

import com.example.auth.oauth.user.SocialUserDetails;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenService {

    public String createToken(Authentication authentication) {
        log.debug("createToken:authentication:{}", authentication);
        SocialUserDetails socialUserDetails = (SocialUserDetails)authentication.getPrincipal();
        log.debug("createToken:socialUserDetails:{}", socialUserDetails);

        Instant nowTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        Date issueTime = Date.from(nowTime);
        Date expirationTime = Date.from(nowTime.plusSeconds(60*60*24*30));

        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .claim("subject", socialUserDetails.getSubject())
                .claim("name", socialUserDetails.getUsername())
                .claim("email", socialUserDetails.getEmail())
                .issuer("example")
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(jwtClaims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            JWSSigner signer = new MACSigner("22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M");
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            log.error("JOSEException", e);
        }

        String token = jwsObject.serialize();
        return token;
    }

    public String getEmail(String token){
        log.debug("getEmail:{}", token);
        return getValue(token, "email");
    }

    private String getValue(String token, String key){
        try {
            SignedJWT signed = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier("22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M22F49U20B06A55B35Z36T78E89A40M");
            if(signed.verify(verifier)){
                return signed.getJWTClaimsSet().getStringClaim(key);
            }
        } catch (ParseException e) {
            log.error("ParseException", e);
        } catch (JOSEException e) {
            log.error("ParseException", e);
        }
        return null;
    }
}
