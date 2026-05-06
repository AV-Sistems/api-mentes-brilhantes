package br.com.avsistems.service;

import br.com.avsistems.dto.request.GiftsRedemptionRequestDto;
import br.com.avsistems.dto.response.GiftsRedemptionResponseDto;
import br.com.avsistems.entity.GiftsEntity;
import br.com.avsistems.entity.GiftsRedemptionEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.repository.GiftsRedemptionRepository;
import br.com.avsistems.repository.GiftsRepository;
import br.com.avsistems.repository.UserRepository;
import br.com.avsistems.type.GiftsRedemptionStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GiftsRedemptionService {

    @Inject
    GiftsRedemptionRepository redemptionRepository;

    @Inject
    GiftsRepository giftsRepository;

    @Inject
    UserRepository userRepository;

    public List<GiftsRedemptionResponseDto> listAll() {
        return redemptionRepository.listAll().stream()
                .map(GiftsRedemptionResponseDto::new)
                .toList();
    }

    public List<GiftsRedemptionResponseDto> listPending() {
        return redemptionRepository.findByStatus(GiftsRedemptionStatus.PENDING).stream()
                .map(GiftsRedemptionResponseDto::new)
                .toList();
    }

    public List<GiftsRedemptionResponseDto> listByUser(UUID userId) {
        return redemptionRepository.findByUserId(userId).stream()
                .map(GiftsRedemptionResponseDto::new)
                .toList();
    }

    /**
     * Usuário solicita resgate de um brinde.
     * O estoque é decrementado imediatamente para reservar o brinde.
     * Os pontos de resgate são descontados imediatamente para evitar novos resgates sem saldo.
     */
    @Transactional
    public GiftsRedemptionResponseDto createRedemption(GiftsRedemptionRequestDto dto) {
        UserEntity user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        GiftsEntity gifts = giftsRepository.findByIdOptional(dto.giftsId())
                .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));

        if (gifts.stock <= 0) {
            throw new BadRequestException("Brinde esgotado. Não é possível realizar o resgate.");
        }

        int userPoints = user.redeemablePoints != null ? user.redeemablePoints : 0;
        if (userPoints < gifts.pointsCost) {
            throw new BadRequestException("Pontos insuficientes. Você possui " + userPoints
                    + " pontos mas o brinde custa " + gifts.pointsCost + " pontos.");
        }

        // Decrementa estoque para reservar o brinde
        gifts.stock = gifts.stock - 1;
        giftsRepository.persist(gifts);

        user.redeemablePoints = userPoints - gifts.pointsCost;
        userRepository.persist(user);

        GiftsRedemptionEntity redemption = new GiftsRedemptionEntity();
        redemption.user = user;
        redemption.gifts = gifts;
        redemption.status = GiftsRedemptionStatus.PENDING;
        redemption.pointsUsed = gifts.pointsCost;

        redemptionRepository.persist(redemption);
        return new GiftsRedemptionResponseDto(redemption);
    }

    /**
     * Admin valida o resgate e marca como VALIDATED.
     * Os pontos já foram descontados na criação.
     */
    @Transactional
    public GiftsRedemptionResponseDto validateRedemption(UUID id) {
        GiftsRedemptionEntity redemption = redemptionRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Resgate não encontrado."));

        if (redemption.status != GiftsRedemptionStatus.PENDING) {
            throw new BadRequestException("Apenas resgates com status PENDING podem ser validados. Status atual: " + redemption.status);
        }

        redemption.status = GiftsRedemptionStatus.VALIDATED;

        redemptionRepository.persist(redemption);
        return new GiftsRedemptionResponseDto(redemption);
    }

    /**
     * Admin cancela o resgate: devolve o estoque do brinde.
     * Devolve também os pontos de resgate que foram reservados no momento da criação.
     */
    @Transactional
    public GiftsRedemptionResponseDto cancelRedemption(UUID id) {
        GiftsRedemptionEntity redemption = redemptionRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Resgate não encontrado."));

        if (redemption.status == GiftsRedemptionStatus.CANCELLED) {
            throw new BadRequestException("Este resgate já está cancelado.");
        }

        UserEntity user = redemption.user;
        user.redeemablePoints = (user.redeemablePoints != null ? user.redeemablePoints : 0) + redemption.pointsUsed;
        userRepository.persist(user);

        // Devolve o estoque ao brinde
        GiftsEntity gifts = redemption.gifts;
        gifts.stock = gifts.stock + 1;
        giftsRepository.persist(gifts);

        redemption.status = GiftsRedemptionStatus.CANCELLED;
        redemptionRepository.persist(redemption);

        return new GiftsRedemptionResponseDto(redemption);
    }
}

