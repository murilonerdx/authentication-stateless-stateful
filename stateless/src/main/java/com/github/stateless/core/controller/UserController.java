package com.github.stateless.core.controller;

import com.github.stateless.core.dto.UsuarioDTO;
import com.github.stateless.core.model.Usuario;
import com.github.stateless.core.services.UsuarioService;
import com.github.stateless.infra.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UsuarioService service;


    @PostMapping()
    public Usuario create(@RequestBody UsuarioDTO usuario)  {
        return service.create(usuario);
    }
}
