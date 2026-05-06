package br.com.avsistems.service;

import br.com.avsistems.dto.request.LiveWithYouDto;
import br.com.avsistems.dto.response.LiveWithYouResponseDto;
import br.com.avsistems.entity.LiveWithYouEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.repository.LiveWithYouRepository;
import br.com.avsistems.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LiveWithYouService {

    @Inject
    LiveWithYouRepository liveWithYouRepository;

    @Inject
    UserRepository userRepository;

    public List<LiveWithYouResponseDto> ListByUserId(UUID id){
        if(userRepository.findByIdOptional(id).isEmpty()){
            throw new ApplicationException("Usuário não encontrado.");
        }

        return liveWithYouRepository.findByUserId(id)
                .stream()
                .map(LiveWithYouResponseDto::new)
                .toList();
    }

    @Transactional
    public List<LiveWithYouResponseDto> create(UUID id, List<LiveWithYouDto> dtos){
        UserEntity user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));

        if (dtos == null || dtos.isEmpty()) {
            throw new ApplicationException("A lista de moradores não pode ser vazia.");
        }

        return dtos.stream().map(dto -> {
            if (dto == null) {
                throw new ApplicationException("Item inválido na lista de moradores.");
            }
            LiveWithYouEntity entity = new LiveWithYouEntity(dto, user);
            liveWithYouRepository.persist(entity);
            return new LiveWithYouResponseDto(entity);
        }).toList();
    }

    @Transactional
    public void delete(UUID id){
        LiveWithYouEntity entity = liveWithYouRepository.findByIdOptional(id)
                .orElseThrow(()-> new ApplicationException("Registro não encontrado."));
        liveWithYouRepository.delete(entity);
    }
}
