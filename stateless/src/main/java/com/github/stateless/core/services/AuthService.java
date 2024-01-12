package com.github.stateless.core.services;

import com.github.stateless.core.dto.AuthRequest;
import com.github.stateless.core.dto.TokenDTO;
import com.github.stateless.core.model.Usuario;
import com.github.stateless.core.repository.UserRepository;
import com.github.stateless.infra.AuthenticationException;
import com.github.stateless.infra.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;

    public TokenDTO login(AuthRequest request) {
        Usuario usuario = repository
                .findByUsername(request.username())
                .orElseThrow(() -> new ValidationException("User not found!"));
        validatePassword(request.password(), usuario.getPassword());
        var accessToken = jwtService.createToken(usuario);
        return new TokenDTO(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder().matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password is incorrect!");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public TokenDTO validateToken(String accessToken) throws AuthenticationException {
        validateExistingToken(accessToken);
        jwtService.validateAccessToken(accessToken);
        return new TokenDTO(accessToken);
    }

    private void validateExistingToken(String accessToken) {
        if (isEmpty(accessToken)) {
            throw new ValidationException("The access token must be informed!");
        }
    }
}