package com.school.management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    
    // Cette clé doit rester secrète car elle sert à signer et vérifier les tokens
    @Value("${jwt.secret}")  
private String secret;

   @Value("${jwt.expiration}")  
private Long expiration;

    
    //  Transforme la clé secrète en objet SecretKey utilisable par HMAC
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    //  Générer un token JWT = <Header en Base64>.<Payload en Base64>.<Signature HMAC>
    public String generateToken(String username) {

        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration))

        // Avec la méthode signWith on crée Signature = HMACSHA256( Base64UrlEncode(Header) + "." + Base64UrlEncode(Payload), SecretKey )
        .signWith(getSigningKey()) 
        
        .compact();
    }
    
    //  Extraire le username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    // Vérifier la validité du token
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    //  Vérifier expiration
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    
    //  Extraction complète des claims (données que on veux transporter)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())   
            .build()
            .parseSignedClaims(token)     
            .getPayload();
    }
}
