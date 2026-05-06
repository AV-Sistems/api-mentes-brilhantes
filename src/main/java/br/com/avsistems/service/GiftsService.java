package br.com.avsistems.service;

import br.com.avsistems.dto.request.GiftsRequestDto;
import br.com.avsistems.dto.response.GiftsResponseDto;
import br.com.avsistems.entity.GiftsEntity;
import br.com.avsistems.entity.PartnersEntity;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.repository.GiftsRepository;
import br.com.avsistems.repository.PartnersRepository;
import br.com.avsistems.type.GiftsStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GiftsService {

    @Inject
    GiftsRepository giftsRepository;

    @Inject
    PartnersRepository partnersRepository;

    public List<GiftsResponseDto> listAll() {
        return giftsRepository.listAll().stream()
                .map(GiftsResponseDto::new)
                .toList();
    }

    public List<GiftsResponseDto> listAvailable() {
        return giftsRepository.findAvailable().stream()
                .map(GiftsResponseDto::new)
                .toList();
    }

    public GiftsResponseDto findById(UUID id) {
        return giftsRepository.findByIdOptional(id)
                .map(GiftsResponseDto::new)
                .orElseThrow(() -> new NotFoundException("Brinde com ID: " + id + " não encontrado."));
    }

    @Transactional
    public GiftsResponseDto createGift(GiftsRequestDto dto) {
        GiftsEntity gifts = new GiftsEntity();
        gifts.name = dto.name();
        gifts.pointsCost = dto.pointsCost();
        gifts.stock = dto.stock() != null ? dto.stock() : 0;
        gifts.status = dto.status() != null ? dto.status() : GiftsStatus.ACTIVE;
        gifts.isAdvanced = dto.isAdvanced() != null ? dto.isAdvanced() : Boolean.FALSE;
        gifts.requeriments = dto.requeriments();

        if (dto.partnerId() != null) {
            PartnersEntity partner = partnersRepository.findByIdOptional(dto.partnerId())
                    .orElseThrow(() -> new NotFoundException("Parceiro não encontrado."));
            gifts.partner = partner;
        }

        giftsRepository.persist(gifts);
        return new GiftsResponseDto(gifts);
    }

    @Transactional
    public GiftsResponseDto updateGift(UUID id, GiftsRequestDto dto) {
        GiftsEntity gifts = giftsRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));

        if (dto.name() != null) gifts.name = dto.name();
        if (dto.pointsCost() != null) gifts.pointsCost = dto.pointsCost();
        if (dto.stock() != null) gifts.stock = dto.stock();
        if (dto.status() != null) gifts.status = dto.status();
        if (dto.isAdvanced() != null) gifts.isAdvanced = dto.isAdvanced();
        if (dto.requeriments() != null) gifts.requeriments = dto.requeriments();
        if (dto.partnerId() != null) {
            PartnersEntity partner = partnersRepository.findByIdOptional(dto.partnerId())
                    .orElseThrow(() -> new NotFoundException("Parceiro não encontrado."));
            gifts.partner = partner;
        }

        return new GiftsResponseDto(gifts);
    }

    @Transactional
    public GiftsResponseDto toggleStatus(UUID id) {
        GiftsEntity gifts = giftsRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));
        gifts.status = gifts.status == GiftsStatus.INACTIVE ? GiftsStatus.ACTIVE : GiftsStatus.INACTIVE;
        return new GiftsResponseDto(gifts);
    }

    @Transactional
    public void deleteGift(UUID id) {
        GiftsEntity gifts = giftsRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));
        giftsRepository.deleteById(gifts.id);
    }
}

