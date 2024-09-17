package com.ecommerce.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    public static final String SECRET="000E3FCCC807F297E6E09574C022525ECB067D43866BC43389C07DAEA094DDEF25C57ED9BC746912C25B71A355325CDC68F508B6F7B3288A7E48121D4961183D";
    public static final Long VALIDITY= TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails){

        Map<String,String> claims=new HashMap<>();
        claims.put("kaashif","http:/kaashifchishti611@gmail.com");

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    public SecretKey generateKey(){

        byte[] decodeKey= Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodeKey);
    }

    public String extractUsername(String jwt){

        Claims claims=Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return claims.getSubject();

    }

    private Claims getClaims(String jwt){

        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public Boolean isTokenValid(String jwt){

        Claims claims=getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));

    }
}
