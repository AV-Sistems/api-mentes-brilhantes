package br.com.avsistems.dto.response;

import br.com.avsistems.entity.CompletedModulesEntity;

import java.util.UUID;

public record CompletedModulesResponseDto(
        UUID id,
        String name
) {
    public CompletedModulesResponseDto(CompletedModulesEntity completedModule) {
        this(
                completedModule.id,
                completedModule.name
        );
    }
}

