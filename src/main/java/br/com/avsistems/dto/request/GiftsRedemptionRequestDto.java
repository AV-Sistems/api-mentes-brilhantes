package br.com.avsistems.dto.request;

import java.util.UUID;

public record GiftsRedemptionRequestDto(
        UUID userId,
        UUID giftsId
) {
}

