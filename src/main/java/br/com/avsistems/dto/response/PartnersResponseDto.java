package br.com.avsistems.dto.response;

import br.com.avsistems.entity.PartnersEntity;

import java.time.LocalDate;
import java.util.UUID;

public record PartnersResponseDto(
        UUID id, String name, String imageUrl, String url, LocalDate validity, String city, String state, String zipCode
) {

    public PartnersResponseDto(PartnersEntity entity){
        this(entity.id, entity.name, entity.imageUrl, entity.url, entity.validity, entity.city, entity.state, entity.zipCode);
    }
}
