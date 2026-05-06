package br.com.avsistems.dto.request;

import java.util.UUID;

public record UserReceivedAwardsRequestDto(
        UUID userId,
        UUID receivedAwardId
) {
}

