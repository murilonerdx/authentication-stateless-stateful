package com.github.stateless.core.controller;


import com.github.stateless.core.dto.AnyResponse;
import com.github.stateless.core.services.AnyService;
import com.github.stateless.infra.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@AllArgsConstructor
@RequestMapping("api/resource")
public class AnyController {

    private final AnyService service;

    @GetMapping
    public AnyResponse getResource(@RequestHeader String accessToken) throws AuthenticationException {
        return service.getData(accessToken);
    }
}
