package br.com.avsistems.service;

import br.com.avsistems.dto.request.PartnerMultipartForm;
import br.com.avsistems.dto.request.PartnersCreateDto;
import br.com.avsistems.dto.response.PartnersResponseDto;
import br.com.avsistems.entity.PartnersEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.repository.PartnersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PartnersService {
    private static final Logger LOG = Logger.getLogger(PartnersService.class);

    @Inject
    PartnersRepository repository;

    @Inject
    FileStorageService storageService;
    @Inject
    ObjectMapper objectMapper;

    public List<PartnersResponseDto> findAll(){
        return repository.listAll().stream().map(PartnersResponseDto::new).toList();
    }

    public List<PartnersResponseDto> FindByValidityAndStateAndCity(String state, String city){
        LocalDate today = LocalDate.now();
        return repository.findByValidatyAndStateAndCity(today, state.toLowerCase(), city.toLowerCase()).stream().map(PartnersResponseDto::new).toList();
    }

    public List<PartnersResponseDto> findAllByValidity (){
        LocalDate today = LocalDate.now();
        return repository.findByValidity(today).stream().map(PartnersResponseDto::new).toList();
    }

    @Transactional
    public PartnersResponseDto createPartner(PartnerMultipartForm form){
        try{
            if (form == null || form.data == null || form.data.isBlank()) {
                throw new BadRequestException("O campo 'data' com os dados do parceiro é obrigatório.");
            }

            PartnersCreateDto partnersCreateDto;
            try {
                partnersCreateDto = objectMapper.readValue(form.data, PartnersCreateDto.class);
            } catch (JsonProcessingException e) {
                LOG.warnf("JSON inválido no campo 'data': %s", e.getMessage());
                throw new BadRequestException("Dados inválidos no campo 'data': " + e.getMessage());
            }

            if(partnersCreateDto.name() == null || partnersCreateDto.name().isBlank()){
                throw new BadRequestException("O nome do parceiro não pode ser vazio.");
            }

        PartnersEntity entity = new PartnersEntity(partnersCreateDto);
        repository.persist(entity);

        FileUpload uploadedFile = form.uploadedFile();
        if(uploadedFile != null && uploadedFile.fileName() != null && !uploadedFile.fileName().isBlank()){
            String path = savePartnerImage(entity.id, uploadedFile);
            entity.imageUrl = path;
            LOG.infof("Imagem salva em: %s", path);
            repository.persist(entity);
        }

            return new PartnersResponseDto(entity);

        }catch (ApplicationException e){
            throw e;
        }catch (Exception e){
            LOG.errorf("Erro inesperado ao criar parceiro: %s", e.getMessage(), e);
            throw new ApplicationException("Erro ao criar parceiro: " + e.getMessage());
        }
    }

    @Transactional
    public PartnersResponseDto updatePartner(UUID id, PartnerMultipartForm form){
        try{
            if (form == null) {
                throw new BadRequestException("Formulário inválido: form não pode ser nulo");
            }

            PartnersEntity entity = repository.findByIdOptional(id)
                    .orElseThrow(() -> new NotFoundException("Nenhum parceiro encontrado com o id: "+id));

            if (form.data != null && !form.data.isBlank()) {
                PartnersCreateDto partnersCreateDto;
                try {
                    partnersCreateDto = objectMapper.readValue(form.data, PartnersCreateDto.class);
                } catch (JsonProcessingException e) {
                    LOG.warnf("JSON inválido no campo 'data': %s", e.getMessage());
                    throw new BadRequestException("Dados inválidos no campo 'data': " + e.getMessage());
                }

                if(partnersCreateDto.name() != null && !partnersCreateDto.name().isBlank()) entity.name = partnersCreateDto.name();
                if(partnersCreateDto.url() != null) entity.url = partnersCreateDto.url();
                if(partnersCreateDto.validity() != null) entity.validity = partnersCreateDto.validity();
                if(partnersCreateDto.city() != null) entity.city = partnersCreateDto.city();
                if(partnersCreateDto.state() != null) entity.state = partnersCreateDto.state();
                if(partnersCreateDto.zipCode() != null) entity.zipCode = partnersCreateDto.zipCode();
            }

            FileUpload uploadedFile = form.uploadedFile();
            if(uploadedFile != null && uploadedFile.fileName() != null && !uploadedFile.fileName().isBlank()){
                String path = savePartnerImage(entity.id, uploadedFile);
                entity.imageUrl = path;
                LOG.infof("Imagem atualizada em: %s", path);
            }

            repository.persist(entity);
            return new PartnersResponseDto(entity);
        }catch (ApplicationException e){
            throw e;
        }catch (Exception e){
            LOG.errorf("Erro inesperado ao editar parceiro: %s", e.getMessage(), e);
            throw new ApplicationException("Erro ao editar parceiro: " + e.getMessage());
        }

    }

    @Transactional
    public void deletePartner(UUID id){
        repository.findByIdOptional(id)
                .ifPresentOrElse(
                    partner -> {
                        try {
                            repository.deleteById(id);
                            LOG.infof("Parceiro com ID %s foi deletado com sucesso", id);
                        } catch (Exception e) {
                            LOG.errorf("Erro ao deletar parceiro com ID %s: %s", id, e.getMessage());
                            throw new ApplicationException("Erro ao deletar parceiro: " + e.getMessage());
                        }
                    },
                    () -> {
                        throw new NotFoundException("Nenhum parceiro encontrado com o id: " + id);
                    }
                );
    }

    private String savePartnerImage(UUID partnerId, FileUpload upload) {
        try {
            byte[] imageBytes = Files.readAllBytes(upload.filePath());
            LOG.infof("Arquivo recebido: %s (%d bytes)", upload.fileName(), imageBytes.length);

            String extension = extractExtension(upload.fileName());
            String fileName = partnerId + extension;
            return storageService.uploadPartners(imageBytes, fileName);
        } catch (IOException e) {
            LOG.errorf("Erro ao salvar imagem: %s", e.getMessage());
            throw new ApplicationException("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return ".bin";
        }
        return fileName.substring(dotIndex);
    }
}
