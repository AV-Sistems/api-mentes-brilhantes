package br.com.avsistems.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record UserUpdateDto (
        String name,
        String email,
        LocalDate dateOfBirth,
        String instagram,
        String educationalInstituition,
        UUID mentesEditionId,
        Integer howPeopleLiveWithYou
){}
