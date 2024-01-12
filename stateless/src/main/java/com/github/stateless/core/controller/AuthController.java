package com.github.stateless.core.controller;

import com.github.stateless.core.dto.AuthRequest;
import com.github.stateless.core.dto.TokenDTO;
import com.github.stateless.core.services.AuthService;
import com.github.stateless.infra.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("login")
    public TokenDTO login(@RequestBody AuthRequest request) {
        return service.login(request);
    }

    @PostMapping("token/validate")
    public TokenDTO validateToken(@RequestHeader String accessToken) throws AuthenticationException {
        return service.validateToken(accessToken);
    }
}
