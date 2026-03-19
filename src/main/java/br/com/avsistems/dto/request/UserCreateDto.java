package br.com.avsistems.dto.request;

import br.com.avsistems.type.UserType;

public record UserCreateDto(
        String name,
        String email,
        String password,
        UserType userType
) {
}
