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
                .claim("member_id", dto.getMember_id())
                .claim("realname", dto.getRealname())
                .claim("nickname", dto.getNickname())
                .claim("password", dto.getPassword())
                .claim("email", dto.getEmail())
                .claim("phone", dto.getPhone())
                .claim("address", dto.getAddress())
                .claim("userauthor", dto.getUserauthor())
                .claim("points", dto.getPoints())
                .claim("ezpay", dto.getEzpay())
                .claim("member_status", dto.getMember_status())
                .claim("member_kick_comment", dto.getMember_kick_comment())
                .claim("social", dto.getSocial())
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