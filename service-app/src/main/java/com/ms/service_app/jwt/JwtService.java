package com.ms.service_app.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
public class JwtService {

    @Value("${security.jwt.modulasBase64}")
    private String modulusBase64;
    @Value("${security.jwt.exponentBase64}")
    private String exponentBase64;

    private final OAuth2ResourceServerProperties properties;

    public JwtService(OAuth2ResourceServerProperties properties) {
        this.properties = properties;
    }


    private RSAPublicKey convertToRSAPublicKey(String modulusBase64, String exponentBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulusBase64);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponentBase64);

        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public String extractEmail(String token){
        try {
            DecodedJWT decodedJWT = buildJWTVerifier().verify(token);
            Claim sub = decodedJWT.getClaim("email");
            return sub.asString();
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeySpecException | JWTVerificationException e) {
            return null;
        }
    }
    public Boolean isValidToken(String token) {
        try {
            DecodedJWT decodedJWT = buildJWTVerifier().verify(token);
            Claim sub = decodedJWT.getClaim("sub");
            Claim aud = decodedJWT.getClaim("aud");
            // if token is valid no exception will be thrown
            System.out.println("Valid TOKEN sub - "+sub.asString());
            System.out.println("Valid TOKEN aud - "+aud.asString());
            return Boolean.TRUE;
        } catch (CertificateException e) {
            //if CertificateException comes from buildJWTVerifier()
            System.err.println("InValid TOKEN");
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (JWTVerificationException e) {
            // if JWT Token in invalid
            System.err.println("InValid TOKEN");
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (Exception e) {
            // If any other exception comes
            System.err.println("InValid TOKEN, Exception Occurred");
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private JWTVerifier buildJWTVerifier() throws CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
        var algo = Algorithm.RSA256(convertToRSAPublicKey(modulusBase64, exponentBase64), null);
        System.out.println("Audience: "+properties.getJwt().getAudiences());
        for (String s : properties.getJwt().getAudiences().toArray(new String[0])) {
            System.out.println(s);
        }
        return JWT.require(algo)
                .withAnyOfAudience(properties.getJwt().getAudiences().toArray(new String[0]))
                .build();
    }
}
