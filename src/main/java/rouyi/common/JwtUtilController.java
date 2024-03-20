package rouyi.common;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import rouyi.entity.Client;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtilController {

    public String generateJwtToken(Integer userId) {
        Client client = new Client();
        String username = client.getPhone();

        long expirationTime = 86400000;
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return token;
    }
}

