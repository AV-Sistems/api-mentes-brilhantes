package br.com.avsistems.service;

import br.com.avsistems.dto.request.UserReceivedAwardsRequestDto;
import br.com.avsistems.dto.response.UserReceivedAwardsResponseDto;
import br.com.avsistems.entity.ReceivedAwardsEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.entity.UserReceivedAwardsEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.repository.ReceivedAwardsRepository;
import br.com.avsistems.repository.UserReceivedAwardsRepository;
import br.com.avsistems.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserReceivedAwardsService {

    @Inject
    UserReceivedAwardsRepository userReceivedAwardsRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ReceivedAwardsRepository receivedAwardsRepository;

    public List<UserReceivedAwardsResponseDto> listAll() {
        return userReceivedAwardsRepository.listAll()
                .stream()
                .map(UserReceivedAwardsResponseDto::new)
                .toList();
    }

    public List<UserReceivedAwardsResponseDto> findByUserId(UUID userId) {
        return userReceivedAwardsRepository.findByUserId(userId)
                .stream()
                .map(UserReceivedAwardsResponseDto::new)
                .toList();
    }

    public List<UserReceivedAwardsResponseDto> findByReceivedAwardId(UUID receivedAwardId) {
        return userReceivedAwardsRepository.findByReceivedAwardId(receivedAwardId)
                .stream()
                .map(UserReceivedAwardsResponseDto::new)
                .toList();
    }

    public UserReceivedAwardsResponseDto findById(UUID id) {
        return userReceivedAwardsRepository.findByIdOptional(id)
                .map(UserReceivedAwardsResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Registro com ID: " + id + " não encontrado."));
    }

    @Transactional
    public UserReceivedAwardsResponseDto create(UserReceivedAwardsRequestDto requestDto) {
        if (requestDto.userId() == null || requestDto.receivedAwardId() == null) {
            throw new BadRequestException("IDs de usuário e prêmio não podem ser nulos.");
        }

        UserEntity user = userRepository.findByIdOptional(requestDto.userId())
                .orElseThrow(() -> new ApplicationException("Usuário com ID: " + requestDto.userId() + " não encontrado."));

        ReceivedAwardsEntity award = receivedAwardsRepository.findByIdOptional(requestDto.receivedAwardId())
                .orElseThrow(() -> new ApplicationException("Prêmio com ID: " + requestDto.receivedAwardId() + " não encontrado."));

        // Verifica se o usuário já recebeu este prêmio
        boolean alreadyReceived = userReceivedAwardsRepository
                .findByUserAndAward(requestDto.userId(), requestDto.receivedAwardId())
                .isPresent();

        if (alreadyReceived) {
            throw new BadRequestException("O usuário já recebeu este prêmio.");
        }

        UserReceivedAwardsEntity entity = new UserReceivedAwardsEntity(user, award);
        userReceivedAwardsRepository.persist(entity);
        return new UserReceivedAwardsResponseDto(entity);
    }

    @Transactional
    public void delete(UUID id) {
        UserReceivedAwardsEntity entity = userReceivedAwardsRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Registro com ID: " + id + " não encontrado."));

        userReceivedAwardsRepository.delete(entity);
    }
}

