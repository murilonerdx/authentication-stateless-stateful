package com.murilonerdx.stateful.core.controller;

import com.murilonerdx.stateful.core.dto.AuthRequest;
import com.murilonerdx.stateful.core.dto.TokenDTO;
import com.murilonerdx.stateful.core.model.Usuario;
import com.murilonerdx.stateful.core.repository.UserRepository;
import com.murilonerdx.stateful.core.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final TokenService service;
    private final UserRepository userRepository;

    @PostMapping()
    public TokenDTO create(@RequestBody AuthRequest usuario)  {
        userRepository.save(new Usuario(null, usuario.username(), usuario.password()));
        return new TokenDTO(service.createToken(usuario.username())) ;
    }
}
