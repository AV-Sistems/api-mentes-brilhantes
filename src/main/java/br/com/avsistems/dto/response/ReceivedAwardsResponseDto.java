package br.com.avsistems.dto.response;

import br.com.avsistems.entity.ReceivedAwardsEntity;

import java.util.UUID;

public record ReceivedAwardsResponseDto(
        UUID id,
        String name,
        String imageUrl
) {
    public ReceivedAwardsResponseDto(ReceivedAwardsEntity receivedAward) {
        this(
                receivedAward.id,
                receivedAward.name,
                receivedAward.imageUrl
        );
    }
}
