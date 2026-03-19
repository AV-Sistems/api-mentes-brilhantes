package br.com.avsistems.dto.request;

import java.util.UUID;

public record TaskUserCompletedRequestDto(
        UUID taskId,
        UUID userId,
        Boolean verified
) {
}
