package br.com.avsistems.dto.request;

import java.time.LocalDate;

public record LiveWithYouDto(
        String name,
        String phone,
        String relationshipType,
        LocalDate dateOfBirth
) {
}
