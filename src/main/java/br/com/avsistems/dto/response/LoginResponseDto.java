package br.com.avsistems.dto.response;

public record LoginResponseDto (
    String token,
    UserResponseDto user
) { }

