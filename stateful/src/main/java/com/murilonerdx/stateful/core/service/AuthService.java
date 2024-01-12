package com.murilonerdx.stateful.core.service;


import com.murilonerdx.stateful.core.dto.AuthRequest;
import com.murilonerdx.stateful.core.dto.AuthUserResponse;
import com.murilonerdx.stateful.core.dto.TokenDTO;
import com.murilonerdx.stateful.core.model.Usuario;
import com.murilonerdx.stateful.core.repository.UserRepository;
import com.murilonerdx.stateful.infra.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public TokenDTO login(AuthRequest request) {
        Usuario usuario = repository
                .findByUsername(request.username())
                .orElseThrow(() -> new ValidationException("User not found!"));
        validatePassword(request.password(), usuario.getPassword());
        var accessToken = tokenService.createToken(usuario.getUsername());
        return new TokenDTO(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password is incorrect!");
        }
    }

    public AuthUserResponse getAuthenticatedUser(String accessToken)  {
        var tokenData = tokenService.getTokenData(accessToken);

        var user = repository.findByUsername(tokenData.username());
        return new AuthUserResponse(user.get().getId(), user.get().getUsername());
    }

    public void logout(String accessToken) {
        tokenService.deleteRedisToken(accessToken);
    }

    public TokenDTO validateToken(String accessToken) throws AuthenticationException {
        validateExistingToken(accessToken);
        tokenService.validateAccessToken(accessToken);
        return new TokenDTO(accessToken);
    }

    private void validateExistingToken(String accessToken) {
        if (isEmpty(accessToken)) {
            throw new ValidationException("The access token must be informed!");
        }
    }
}