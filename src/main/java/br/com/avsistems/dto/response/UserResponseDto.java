package br.com.avsistems.dto.response;

import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.type.UserType;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        UserType userType,
        Integer totalPoints
) {
    // Este é o construtor compacto que converte a Entity para DTO
    public UserResponseDto(UserEntity userEntity) {
        this(
                userEntity.id,
                userEntity.name,
                userEntity.email,
                userEntity.userType,
                userEntity.totalPoints
        );
    }
}