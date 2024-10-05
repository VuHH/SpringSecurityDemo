package com.demo.springsecurity.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
  private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateToken(String username) {
    Instant now = Instant.now();
    long JWT_EXPIRATION = 604800000L;
    Instant expiryDate = Instant.ofEpochMilli(now.toEpochMilli() + JWT_EXPIRATION);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiryDate))
        .signWith(key)
        .compact();
  }

  public String getUsernameFromJWT(String token) {

    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}
