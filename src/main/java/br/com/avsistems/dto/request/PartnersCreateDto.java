package br.com.avsistems.dto.request;

import java.time.LocalDate;

public record PartnersCreateDto(
        String name, String url, LocalDate validity, String city, String state, String zipCode
){
}
