package com.bvp.task.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.access-expiration-time-in-minutes}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh-expiration-time-in-minutes}")
    private long refreshTokenExpirationTime;

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        claims.put("access", true);

        Date issuedTime = new Date();
        Date expireDate = new Date(issuedTime.getTime() + calculateExpirationTimeInMillis(accessTokenExpirationTime));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedTime)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        claims.put("refresh", true);

        Date issuedTime = new Date();
        Date expireDate = new Date(issuedTime.getTime() + calculateExpirationTimeInMillis(refreshTokenExpirationTime));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedTime)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token, boolean isAccess) {
        try {
            Claims claims = getAllClaimsFromToken(token);

            if (claims.getExpiration().before(new Date())) {
                throw new BadCredentialsException("Error validating token: Token has expired");
            }

            if (isAccess && claims.containsKey("access")) {
                return true;
            } else {
                return !isAccess && claims.containsKey("refresh");
            }
        } catch (ExpiredJwtException e) {
            log.error("Error validating token: " + e.getMessage());
            return false;
        }
    }

    public List<String> getRoles(String token) {
        List<?> roles = getAllClaimsFromToken(token).get("roles", List.class);

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private int calculateExpirationTimeInMillis(long minutes) {
        return (int) (minutes * 60 * 1000);
    }
}