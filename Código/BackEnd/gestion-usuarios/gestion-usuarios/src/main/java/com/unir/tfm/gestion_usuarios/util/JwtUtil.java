package com.unir.tfm.gestion_usuarios.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "EstaEsUnaClaveSuperSeguraDe32Caracteres";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email, String role) {
        String prefixedRole = "ROLE_" + role;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", prefixedRole);
        return Jwts.builder()
                .setSubject(email)
                .claim("role", prefixedRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
                
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("token expirado: " + e.getMessage());
            
            return false;
        } catch (Exception e) {
            System.out.println("Token invalido: " + e.getMessage());
            return false;
        }   
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = getClaims(token);
        String role = (String) claims.get("role");
        System.out.println("roleee" + role);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token expirado: " + e.getMessage());
        } catch (@SuppressWarnings("deprecation") io.jsonwebtoken.SignatureException e) {
            System.out.println("Firma del token inválida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general al extraer los claims: " + e.getMessage());
        }
        return null;
    }
    

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token expirado: " + e.getMessage());
            return true; // Si la excepción se lanza, el token ya expiró
        } catch (Exception e) {
            System.out.println("Error al verificar la expiración del token: " + e.getMessage());
            return true; // Si ocurre otro error, también lo tratamos como expirado
        }
    }
    
}
 