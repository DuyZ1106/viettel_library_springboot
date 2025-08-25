package com.example.library.security;

import com.example.library.entity.Permission;
import com.example.library.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final String JWT_SECRET = "my-super-secret-key-for-jwt-token-generation-must-be-at-least-256-bits";
    private static final long JWT_EXPIRATION_MS = 24 * 60 * 60 * 1000;
    private final SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    /** Sinh JWT từ user */
    public String generateToken(User user) {
        List<String> authorities = user.getRoleGroup().getPermissions().stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** Lấy username từ token */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /** Kiểm tra token hợp lệ */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
            return false;
        }
    }
}
