package br.com.avsistems.dto.request;

import br.com.avsistems.type.GiftsStatus;

import java.util.UUID;

public record GiftsRequestDto(
        String name,
        Integer pointsCost,
        Integer stock,
        GiftsStatus status,
        UUID partnerId,
        Boolean isAdvanced,
        String requeriments
) {
}

