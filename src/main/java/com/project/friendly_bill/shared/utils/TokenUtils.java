package com.project.friendly_bill.shared.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.shared.exception.ErrorCode;
import com.project.friendly_bill.shared.exception.FBException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TokenUtils {
    @Value("${application.security.jwt.secret-key:LgyfTFgfKshEqJEMvXLV0Y2uKDPcOTrW}")
    String SECRET_KEY;

    @Value("${spring.security.oauth2.resourceserver.jwt.expiration:900000}")
    public Long EXPIRATION;

    @Value("${spring.security.oauth2.resourceserver.jwt.refresh-expiration:604800000}")
    public Long REFRESH_EXPIRATION;

    String seed = UUID.randomUUID().toString();

    final CacheUtils cacheUtils;

    public String generateAccessToken(User user) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .jwtID(seed)
                .claim("scope", user.getRole().name())
                .claim("type", "access-token")
                .issuer("https://friendly-bill.com")
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION))
                .issueTime(new Date())
                .build();

        return signClaims(claims);
    }

    public String generateRefreshToken(User user) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .claim("access_jti", seed)
                .claim("type", "refresh-token")
                .issuer("https://friendly-bill.com")
                .expirationTime(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .issueTime(new Date())
                .build();

        return signClaims(claims);
    }

    private String signClaims(JWTClaimsSet claims) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public boolean verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier))
                return false;

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            if (cacheUtils.existsInCache("blacklist:" + claims.getJWTID()))
                return false;

            Date expiration = claims.getExpirationTime();
            if (expiration.before(new Date()))
                return false;

            return true;
        } catch (JOSEException | ParseException e) {
            throw new FBException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Optional<JWTClaimsSet> extractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier))
                return Optional.empty();
            return Optional.of(signedJWT.getJWTClaimsSet());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
