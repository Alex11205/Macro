package com.alex.macro;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {

//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println("Your JWT Secret Key: " + encodedKey);

        SecretKey key = Jwts.SIG.HS256.key().build();
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(base64Key);
    }
}
