package com.bankingapp.banking_management_system.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // Builds the signing key from our secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generates a token containing the user's email (as "subject") and role
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)          // custom claim: we'll read this to check authorization
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();                   // produces the final xxxxx.yyyyy.zzzzz string
    }

    // Extracts the email (subject) from a token
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Extracts the role claim from a token
    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // Checks if a token is expired
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    // Full validation: signature valid AND not expired AND matches the given email
    public boolean isTokenValid(String token, String expectedEmail) {
        try {
            String email = extractEmail(token);
            return email.equals(expectedEmail) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // any parsing/signature error = invalid token
        }
    }
}
