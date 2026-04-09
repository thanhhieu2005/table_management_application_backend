package com.example.table_management_application.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.table_management_application.security.custom.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  
  private final String SECRET = "table-management-application-secret-key";
  private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14;

  private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(CustomUserDetails userDetails) {
      return Jwts.builder()
          .setSubject(userDetails.getUsername())

          // optional: add roles
          .claim("roles", userDetails.getAuthorities().stream()
              .map(auth -> auth.getAuthority())
              .collect(Collectors.toSet()))

          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))

          .signWith(getSignKey(), SignatureAlgorithm.HS256)
          .compact();
    }
  
    // 🔍 Extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 🔍 Validate token
    public boolean isTokenValid(String token, CustomUserDetails userDetails) {

        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

    // 🔍 Check expiration
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // 🔍 Parse claims
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
