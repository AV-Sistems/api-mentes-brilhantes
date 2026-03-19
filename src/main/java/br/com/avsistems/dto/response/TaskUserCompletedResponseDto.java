package br.com.avsistems.dto.response;

import br.com.avsistems.entity.TaskUserCompletedEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskUserCompletedResponseDto(
        UUID id,
        TaskResponseDto task,
        UserResponseDto user,
        Boolean verified,
        LocalDateTime updatedAt
) {
    public TaskUserCompletedResponseDto(TaskUserCompletedEntity entity) {
        this(
                entity.id,
                new TaskResponseDto(entity.tasksEntity), // Converte Entidade para DTO
                new UserResponseDto(entity.userEntity),  // Converte Entidade para DTO
                entity.verified,
                entity.updatedAt
        );
    }
}