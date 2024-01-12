package com.murilonerdx.stateful.core.controller;


import com.murilonerdx.stateful.core.dto.AuthRequest;
import com.murilonerdx.stateful.core.dto.AuthUserResponse;
import com.murilonerdx.stateful.core.dto.TokenDTO;
import com.murilonerdx.stateful.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestHeader String accessToken) {
        service.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("token/validate")
    public TokenDTO validateToken(@RequestHeader String accessToken) throws AuthenticationException {
        return service.validateToken(accessToken);
    }


    @GetMapping("user")
    public AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken) {
        return service.getAuthenticatedUser(accessToken);
    }
}
