package br.com.avsistems.dto.response;

import br.com.avsistems.entity.UserCompletedModulesEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCompletedModulesResponseDto(
        UUID id,
        UUID userId,
        String userName,
        UUID completedModuleId,
        String completedModuleName,
        LocalDateTime completedDate
) {
    public UserCompletedModulesResponseDto(UserCompletedModulesEntity userCompletedModule) {
        this(
                userCompletedModule.id,
                userCompletedModule.user.id,
                userCompletedModule.user.name,
                userCompletedModule.completedModule.id,
                userCompletedModule.completedModule.name,
                userCompletedModule.completedDate
        );
    }
}

