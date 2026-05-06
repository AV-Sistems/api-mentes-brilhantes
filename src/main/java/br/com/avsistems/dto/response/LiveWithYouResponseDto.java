package br.com.avsistems.dto.response;

import br.com.avsistems.entity.LiveWithYouEntity;

import java.time.LocalDate;

public record LiveWithYouResponseDto(
        String name,
        String phone,
        String relationshipType,
        LocalDate dateOfBirth
) {
    public LiveWithYouResponseDto(LiveWithYouEntity entity) {
        this(
                entity.name,
                entity.phone,
                entity.relationshipType,
                entity.dateOfBirth
        );
    }
}
