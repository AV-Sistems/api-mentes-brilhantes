package br.com.avsistems.service;

import br.com.avsistems.dto.request.MentesEditionRequestDto;
import br.com.avsistems.dto.response.MentesEditionResponseDto;
import br.com.avsistems.entity.MentesEditionEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.repository.MentesEditionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MentesEditionService {

    @Inject
    MentesEditionRepository repository;

    @Transactional
    public MentesEditionResponseDto createMentesEdition (MentesEditionRequestDto mentesDto){
        try{
            MentesEditionEntity entity = new MentesEditionEntity(mentesDto);
            repository.persist(entity);
            return new MentesEditionResponseDto(entity);
        } catch (Exception e){
            throw new RuntimeException("Erro ao criar Edição do Mentes Brilhantes: "+e);
        }
    }

    public List<MentesEditionResponseDto> listAllMentesEdition(){
        return repository.listAll().stream().map(MentesEditionResponseDto::new).toList();
    }

    public List<MentesEditionResponseDto> findByStateAndCity(String state, String city){
        return repository.findByStateAndCity(state.toLowerCase(), city.toLowerCase()).stream().map(MentesEditionResponseDto::new).toList();
    }

    public MentesEditionResponseDto findByName(String title) {
        if (title == null || title.isBlank()) {
            throw new ApplicationException("O título para pesquisa não pode ser vazio.");
        }

        return repository.findByTitle(title.toLowerCase())
                .map(MentesEditionResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Nenhuma edição do Mentes Brilhantes com o nome pesquisado."));
    }

    @Transactional
    public MentesEditionResponseDto updateMentes(UUID id, MentesEditionRequestDto mentesDto){
        MentesEditionEntity entity = repository.findByIdOptional(id)
                .orElseThrow(()-> new ApplicationException("Nenhuma edição do Mentes Brilhantes encontrada para o id: "+id+"."));
        try{

            if(mentesDto.title() != null) entity.title = mentesDto.title();
            if(mentesDto.dateEdition() != null) entity.dateEdition = mentesDto.dateEdition();
            if(mentesDto.zipCode() != null)  entity.zipCode = mentesDto.zipCode();
            if(mentesDto.city() != null) entity.city = mentesDto.city();
            if(mentesDto.state() != null) entity.state = mentesDto.state();

            repository.persist(entity);

            return new MentesEditionResponseDto(entity);
        }catch (Exception e){
            throw new RuntimeException("Erro ao editar Edição do Mentes Brilhantes: "+e);

        }
    }

    @Transactional
    public void deleteMentes(UUID id){
        if(repository.findByIdOptional(id).isEmpty()){
            throw new ApplicationException("Não foi encontrado Edição do Mentes Brilhantes com o id: "+id+".");
        }
        repository.deleteById(id);
    }
}
