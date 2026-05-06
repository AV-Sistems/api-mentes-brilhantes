package br.com.avsistems.dto.request;

import java.time.LocalDate;

public record MentesEditionRequestDto(
        String title,
        LocalDate dateEdition,
        String zipCode,
        String city,
        String state
) {
}
