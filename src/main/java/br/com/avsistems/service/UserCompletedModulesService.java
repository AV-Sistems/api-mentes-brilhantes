package br.com.avsistems.service;

import br.com.avsistems.dto.request.UserCompletedModulesRequestDto;
import br.com.avsistems.dto.response.UserCompletedModulesResponseDto;
import br.com.avsistems.entity.CompletedModulesEntity;
import br.com.avsistems.entity.UserCompletedModulesEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.repository.CompletedModulesRepository;
import br.com.avsistems.repository.UserCompletedModulesRepository;
import br.com.avsistems.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserCompletedModulesService {

    @Inject
    UserCompletedModulesRepository userCompletedModulesRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CompletedModulesRepository completedModulesRepository;

    public List<UserCompletedModulesResponseDto> listAll() {
        return userCompletedModulesRepository.listAll()
                .stream()
                .map(UserCompletedModulesResponseDto::new)
                .toList();
    }

    public List<UserCompletedModulesResponseDto> findByUserId(UUID userId) {
        return userCompletedModulesRepository.findByUserId(userId)
                .stream()
                .map(UserCompletedModulesResponseDto::new)
                .toList();
    }

    public List<UserCompletedModulesResponseDto> findByCompletedModuleId(UUID completedModuleId) {
        return userCompletedModulesRepository.findByCompletedModuleId(completedModuleId)
                .stream()
                .map(UserCompletedModulesResponseDto::new)
                .toList();
    }

    public UserCompletedModulesResponseDto findById(UUID id) {
        return userCompletedModulesRepository.findByIdOptional(id)
                .map(UserCompletedModulesResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Registro com ID: " + id + " não encontrado."));
    }

    @Transactional
    public UserCompletedModulesResponseDto create(UserCompletedModulesRequestDto requestDto) {
        if (requestDto.userId() == null || requestDto.completedModuleId() == null) {
            throw new BadRequestException("IDs de usuário e módulo não podem ser nulos.");
        }

        UserEntity user = userRepository.findByIdOptional(requestDto.userId())
                .orElseThrow(() -> new ApplicationException("Usuário com ID: " + requestDto.userId() + " não encontrado."));

        CompletedModulesEntity module = completedModulesRepository.findByIdOptional(requestDto.completedModuleId())
                .orElseThrow(() -> new ApplicationException("Módulo com ID: " + requestDto.completedModuleId() + " não encontrado."));

        // Verifica se o usuário já completou este módulo
        boolean alreadyCompleted = userCompletedModulesRepository
                .findByUserAndModule(requestDto.userId(), requestDto.completedModuleId())
                .isPresent();

        if (alreadyCompleted) {
            throw new BadRequestException("O usuário já completou este módulo.");
        }

        UserCompletedModulesEntity entity = new UserCompletedModulesEntity(user, module);
        userCompletedModulesRepository.persist(entity);
        return new UserCompletedModulesResponseDto(entity);
    }

    @Transactional
    public void delete(UUID id) {
        UserCompletedModulesEntity entity = userCompletedModulesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Registro com ID: " + id + " não encontrado."));

        userCompletedModulesRepository.delete(entity);
    }
}

