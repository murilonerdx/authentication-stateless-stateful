package com.github.stateless.core.dto;

public record AnyResponse(String status, Integer code, AuthUserResponse authUser) {
}
