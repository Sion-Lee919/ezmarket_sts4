package com.ezmarket.cookie;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;

import ezmarket.MemberDTO;

import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "Hd5HJsDA5d5SDd523adfDF4233AFHd5HJsDA5d5SDd523adfDF4233AFFssdf42124fADSFa";
    private static final long EXPIRATION_TIME = 86400000;
    private static final Key SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName());

    public static String generateToken(MemberDTO dto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(dto.getUsername()) 
                .claim("realname", dto.getRealname())
                .claim("phone", dto.getPhone())
                .claim("email", dto.getEmail())
                .claim("address", dto.getAddress())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)  
                .compact();
    }

    public static String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)  
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); 
    }
}