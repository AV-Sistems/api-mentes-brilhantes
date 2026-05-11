package br.com.avsistems.dto.request;

import java.util.List;
import java.util.UUID;

public record UserCreateDto(
        String name,
        String email,
        String password,
        List<UUID> completedModuleIds,
        List<UUID> receivedAwardIds,
        UUID mentesEditionId
) {
}
