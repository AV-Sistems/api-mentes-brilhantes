package br.com.avsistems.dto.request;

import br.com.avsistems.type.UserType;

public record UserUpdateDto (
        String name,
        String email,
        UserType userType
){}
