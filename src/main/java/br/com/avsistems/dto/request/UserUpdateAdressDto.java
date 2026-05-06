package br.com.avsistems.dto.request;

public record UserUpdateAdressDto(
        String street,
        String number,
        String neigborhood,
        String city,
        String state,
        String complement,
        String zipCode
) {
}
