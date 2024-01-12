package com.murilonerdx.stateful.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilonerdx.stateful.core.dto.TokenData;
import com.murilonerdx.stateful.infra.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class TokenService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Long ONE_DAY_IN_SECONDS = 86400L;
    
    
    private final RedisTemplate<String, String> redisTemplate;
    
    private final ObjectMapper objectMapper;
    
    public String createToken(String username){
        var accessToken = UUID.randomUUID().toString();
        var data = new TokenData(username);

        var jsonData = getJsonData(data);
        redisTemplate.opsForValue().set(accessToken, jsonData);
        redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));

        return accessToken;
    }


    private String getJsonData(Object payload){
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
           return "";
        }
    }

    private Object parseJsonObjectData(String payload, Class<?> object) throws AuthenticationException {
        try {
            return objectMapper.readValue(payload, object);
        } catch (Exception e) {
            return "";
        }
    }

    private String getRedisTokenValue(String accessToken){
        return redisTemplate.opsForValue().get(accessToken);
    }

    public TokenData getTokenData(String token) throws AuthenticationException {
        var accessToken = extractToken(token);
        return (TokenData) parseJsonObjectData(getRedisTokenValue(accessToken), TokenData.class);
    }

    public boolean validateAccessToken(String token){
        var tokenData = extractToken(token);
        var data = getRedisTokenValue(tokenData);

        return !isEmpty(data);
    }
    
    public Boolean deleteRedisToken(String token){
        if(validateAccessToken(token)){
            var accessToken = extractToken(token);
            return redisTemplate.delete(accessToken);
        }
        return Boolean.FALSE;
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
    
    
}
