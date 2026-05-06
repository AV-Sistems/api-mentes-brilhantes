package br.com.avsistems.dto.response;

import br.com.avsistems.entity.GiftsEntity;
import br.com.avsistems.type.GiftsStatus;

import java.util.UUID;

public record GiftsResponseDto(
        UUID id,
        String name,
        Integer pointsCost,
        Boolean isAdvanced,
        Integer stock,
        GiftsStatus status,
        UUID partnerId,
        String partnerName
) {

    public GiftsResponseDto(GiftsEntity entity) {
        this(
                entity.id,
                entity.name,
                entity.pointsCost,
                entity.isAdvanced,
                entity.stock,
                entity.status,
                entity.partner != null ? entity.partner.id : null,
                entity.partner != null ? entity.partner.name : null
        );
    }
}

