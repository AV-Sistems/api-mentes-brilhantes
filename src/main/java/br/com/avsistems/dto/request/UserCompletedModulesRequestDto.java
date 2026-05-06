package br.com.avsistems.dto.request;

import java.util.UUID;

public record UserCompletedModulesRequestDto(
        UUID userId,
        UUID completedModuleId
) {
}

