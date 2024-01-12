package com.github.stateless.core.services;

import com.github.stateless.core.dto.AuthUserResponse;
import com.github.stateless.core.model.Usuario;
import com.github.stateless.infra.AuthenticationException;
import com.github.stateless.infra.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Integer ONE_DAY_IN_HOURS = 24;

    @Value("${app.token.secret-key}")
    private String secretKey;

    public String createToken(Usuario user) {
        var data = new HashMap<String, String>();
        data.put("id", user.getId().toString());
        data.put("username", user.getUsername());
        return Jwts
                .builder()
                .claims(data)
                .expiration(generateExpiresAt())
                .signWith(generateSign())
                .compact();
    }

    public Date generateExpiresAt() {
        return Date.from(
                LocalDateTime.now()
                        .plusHours(ONE_DAY_IN_HOURS)
                        .atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    public void validateAccessToken(String token) throws AuthenticationException {
        var accessToken = extractToken(token);
        try {
            Jwts
                    .parser()
                    .verifyWith(generateSign())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token: " + ex.getMessage());
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }

    private Claims getClaims(String token) throws AuthenticationException {
        var accessToken = extractToken(token);
        try {
            return Jwts
                    .parser()
                    .verifyWith(generateSign())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token: " + ex.getMessage());
        }
    }

    public AuthUserResponse getAuthenticatedUser(String token) throws AuthenticationException {
        var tokenClains = getClaims(token);
        var userId = Integer.valueOf((String) tokenClains.get("id"));
        return new AuthUserResponse(userId, (String) tokenClains.get("username"));
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
