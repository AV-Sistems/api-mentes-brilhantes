package br.com.avsistems.dto.response;

import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.type.UserType;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        UserType userType,
        Integer totalPoints,
        Integer redeemablePoints,
        String imageUrl,
        String educationalInstituition,
        String instagram,
        LocalDate dateOfBirth,
        MentesEditionResponseDto mentesEdition,
        Integer howPeopleLiveWithYou,

        String street,
        String number,
        String neigborhood,
        String city,
        String state,
        String complement,
        String zipCode
) {

    // Este é o construtor compacto que converte a Entity para DTO
    public UserResponseDto(UserEntity userEntity) {
        this(
                userEntity.id,
                userEntity.name,
                userEntity.email,
                userEntity.userType,
                userEntity.totalPoints,
                userEntity.redeemablePoints,
                userEntity.urlImage,
                userEntity.educationalInstituition,
                userEntity.instagram,
                userEntity.dateOfBirth,
                userEntity.mentesEdition != null ? new MentesEditionResponseDto(userEntity.mentesEdition) : null,
                userEntity.howPeopleLiveWithYou,
                userEntity.street,
                userEntity.number,
                userEntity.neigborhood,
                userEntity.city,
                userEntity.state,
                userEntity.complement,
                userEntity.zipCode
        );
    }
}