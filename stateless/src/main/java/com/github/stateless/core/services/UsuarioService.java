package com.github.stateless.core.services;

import com.github.stateless.core.dto.UsuarioDTO;
import com.github.stateless.core.model.Usuario;
import com.github.stateless.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UserRepository usuarioRepository;
    private AuthService authService;

    public Usuario create(UsuarioDTO usuarioDTO) {
        return usuarioRepository.save(new Usuario(null, usuarioDTO.name(), authService.passwordEncoder().encode(usuarioDTO.password())));
    }
}
