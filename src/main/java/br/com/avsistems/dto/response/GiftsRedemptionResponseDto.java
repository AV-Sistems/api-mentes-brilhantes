package br.com.avsistems.dto.response;

import br.com.avsistems.entity.GiftsRedemptionEntity;
import br.com.avsistems.type.GiftsRedemptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record GiftsRedemptionResponseDto(
        UUID id,
        GiftsResponseDto gifts,
        UserResponseDto user,
        GiftsRedemptionStatus status,
        Integer pointsUsed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public GiftsRedemptionResponseDto(GiftsRedemptionEntity entity) {
        this(
                entity.id,
                new GiftsResponseDto(entity.gifts),
                new UserResponseDto(entity.user),
                entity.status,
                entity.pointsUsed,
                entity.createdAt,
                entity.updatedAt
        );
    }
}

