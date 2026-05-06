package br.com.avsistems.service;

import br.com.avsistems.dto.request.CompletedModulesRequestDto;
import br.com.avsistems.dto.response.CompletedModulesResponseDto;
import br.com.avsistems.entity.CompletedModulesEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.repository.CompletedModulesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CompletedModulesService {

    @Inject
    CompletedModulesRepository completedModulesRepository;

    public List<CompletedModulesResponseDto> listAll() {
        return completedModulesRepository.listAll()
                .stream()
                .map(CompletedModulesResponseDto::new)
                .toList();
    }

    public List<CompletedModulesResponseDto> findByName(String name) {
        return completedModulesRepository.findByName(name)
                .stream()
                .map(CompletedModulesResponseDto::new)
                .toList();
    }

    public CompletedModulesResponseDto findById(UUID id) {
        return completedModulesRepository.findByIdOptional(id)
                .map(CompletedModulesResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Módulo com ID: " + id + " não encontrado."));
    }

    @Transactional
    public CompletedModulesResponseDto create(CompletedModulesRequestDto requestDto) {
        if (requestDto.name() == null || requestDto.name().trim().isEmpty()) {
            throw new BadRequestException("O nome do módulo não pode estar vazio.");
        }

        CompletedModulesEntity entity = new CompletedModulesEntity(requestDto.name());
        completedModulesRepository.persist(entity);
        return new CompletedModulesResponseDto(entity);
    }

    @Transactional
    public CompletedModulesResponseDto update(UUID id, CompletedModulesRequestDto requestDto) {
        CompletedModulesEntity entity = completedModulesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Módulo com ID: " + id + " não encontrado."));

        if (requestDto.name() != null && !requestDto.name().trim().isEmpty()) {
            entity.name = requestDto.name();
        }

        completedModulesRepository.persist(entity);
        return new CompletedModulesResponseDto(entity);
    }

    @Transactional
    public void delete(UUID id) {
        CompletedModulesEntity entity = completedModulesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Módulo com ID: " + id + " não encontrado."));

        completedModulesRepository.delete(entity);
    }
}

