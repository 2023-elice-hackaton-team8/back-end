package com.elice.hackathon.security;

import com.elice.hackathon.global.common.ErrorMessage;
import com.elice.hackathon.global.error.BusinessException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@PropertySource("classpath:jwt.yml")
@Service
@Slf4j
public class TokenProvider {
    private static String secretKey;
    private final long accessExpirationHours;;
    private final String issuer;

    private static final long ACCESS_TIME =  60 * 1000L;
    private static final long REFRESH_TIME =  2 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    public TokenProvider(
            @Value("${secret-key}") String secretKey,
            @Value("${access-expiration-hours}") long accessExpirationHours,
            @Value("${issuer}") String issuer
    ) {
        TokenProvider.secretKey = secretKey;
        this.accessExpirationHours = accessExpirationHours;
        this.issuer = issuer;
    }

    public String createAccessToken(String userSpecification) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(userSpecification)  // JWT 토큰 제목
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(accessExpirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getTokenSubject(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e) {
           return e.getClaims().getSubject();
        }
    }


    // 토큰 검증
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
    private static Claims getClaimsFormToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token).getBody();

            return claims;
        } catch (Exception e){
            throw new BusinessException(ErrorMessage.INVAILID_JWT_ACCESS_TOKEN);
        }
    }

    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("sub").toString().split(":")[0];
    }

    public static long getExpDateFromToken(String token) {
        Claims claims = getClaimsFormToken(token);

        return Long.parseLong(claims.get("exp").toString());
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }
}

