package com.pyurtaev.banking.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "ZHNmZ2RzZnYzZnZkc2Z2bmk3ZXI0aHZpd2U4anI0aW84M3I=";
    private static final String USER_ID_CLAIM = "user_id";

    public String extractUserId(String token) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get(USER_ID_CLAIM));
    }

    public String generateToken(final Long userId) {
        return Jwts
                .builder()
                .setClaims(Map.of(USER_ID_CLAIM, userId))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}