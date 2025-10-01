package com.example.restaurantMap.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

   @Value("${jwt.serect}")
    private String serect;

   @Value("${jwt.expriation")
    private Long expiration;

   private Key getSigningKey(){
       return Keys.hmacShaKeyFor(serect.getBytes());
   }

   public String generateToken(UserDetails userDetails){
       return Jwts.builder()
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + expiration))
               .signWith(getSigningKey(), SignatureAlgorithm.HS256)
               .compact();
   }

   public String getUsernameFromToken(String token){
       return Jwts.parserBuilder()
               .setSigningKey(getSigningKey())
               .build()
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
   }

   public boolean isTokenExpired(String token){
       return Jwts.parserBuilder()
               .setSigningKey(getSigningKey())
               .build()
               .parseClaimsJws(token)
               .getBody()
               .getExpiration()
               .before(new Date());
   }

   public boolean validateToken(String token, UserDetails userDetails){
       final String username = getUsernameFromToken(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }
}
