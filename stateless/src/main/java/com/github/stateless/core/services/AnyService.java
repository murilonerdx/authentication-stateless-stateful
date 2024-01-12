package com.github.stateless.core.services;

import com.github.stateless.core.dto.AnyResponse;
import com.github.stateless.infra.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

    private final JwtService jwtService;

    public AnyResponse getData(String accessToken) throws AuthenticationException {
        jwtService.validateAccessToken(accessToken);
        var authUser = jwtService.getAuthenticatedUser(accessToken);
        var ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authUser);
    }
}
