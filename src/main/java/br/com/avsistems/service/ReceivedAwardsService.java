package br.com.avsistems.service;

import br.com.avsistems.dto.request.ReceivedAwardsMultipartForm;
import br.com.avsistems.dto.request.ReceivedAwardsRequestDto;
import br.com.avsistems.dto.response.ReceivedAwardsResponseDto;
import br.com.avsistems.entity.ReceivedAwardsEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.repository.ReceivedAwardsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ReceivedAwardsService {

    private static final Logger LOG = Logger.getLogger(ReceivedAwardsService.class);

    @Inject
    ReceivedAwardsRepository receivedAwardsRepository;

    @Inject
    FileStorageService storageService;

    @Inject
    ObjectMapper objectMapper;

    public List<ReceivedAwardsResponseDto> listAll() {
        return receivedAwardsRepository.listAll()
                .stream()
                .map(ReceivedAwardsResponseDto::new)
                .toList();
    }

    public List<ReceivedAwardsResponseDto> findByName(String name) {
        return receivedAwardsRepository.findByName(name)
                .stream()
                .map(ReceivedAwardsResponseDto::new)
                .toList();
    }

    public ReceivedAwardsResponseDto findById(UUID id) {
        return receivedAwardsRepository.findByIdOptional(id)
                .map(ReceivedAwardsResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Prêmio com ID: " + id + " não encontrado."));
    }

    @Transactional
    public ReceivedAwardsResponseDto create(ReceivedAwardsMultipartForm form) {
        try {
            ReceivedAwardsRequestDto requestDto = parseRequiredRequest(form);

            ReceivedAwardsEntity entity = new ReceivedAwardsEntity(requestDto.name(), null);
            receivedAwardsRepository.persist(entity);

            FileUpload uploadedFile = form.uploadedFile();
            if (uploadedFile != null && uploadedFile.fileName() != null && !uploadedFile.fileName().isBlank()) {
                entity.imageUrl = saveAwardImage(entity.id, uploadedFile);
            }

            receivedAwardsRepository.persist(entity);
            return new ReceivedAwardsResponseDto(entity);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf("Erro inesperado ao criar prêmio recebido: %s", e.getMessage(), e);
            throw new ApplicationException("Erro ao criar prêmio recebido: " + e.getMessage());
        }
    }

    @Transactional
    public ReceivedAwardsResponseDto update(UUID id, ReceivedAwardsMultipartForm form) {
        ReceivedAwardsEntity entity = receivedAwardsRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Prêmio com ID: " + id + " não encontrado."));

        try {
            if (form != null && form.data != null && !form.data.isBlank()) {
                ReceivedAwardsRequestDto requestDto = parseOptionalRequest(form);
                if (requestDto.name() != null && !requestDto.name().trim().isEmpty()) {
                    entity.name = requestDto.name();
                }
            }

            FileUpload uploadedFile = form != null ? form.uploadedFile() : null;
            if (uploadedFile != null && uploadedFile.fileName() != null && !uploadedFile.fileName().isBlank()) {
                entity.imageUrl = saveAwardImage(entity.id, uploadedFile);
            }

            receivedAwardsRepository.persist(entity);
            return new ReceivedAwardsResponseDto(entity);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf("Erro inesperado ao editar prêmio recebido: %s", e.getMessage(), e);
            throw new ApplicationException("Erro ao editar prêmio recebido: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(UUID id) {
        ReceivedAwardsEntity entity = receivedAwardsRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Prêmio com ID: " + id + " não encontrado."));

        receivedAwardsRepository.delete(entity);
    }

    private ReceivedAwardsRequestDto parseRequiredRequest(ReceivedAwardsMultipartForm form) {
        if (form == null || form.data == null || form.data.isBlank()) {
            throw new BadRequestException("O campo 'data' com os dados do prêmio é obrigatório.");
        }
        return parseRequest(form.data);
    }

    private ReceivedAwardsRequestDto parseOptionalRequest(ReceivedAwardsMultipartForm form) {
        return parseRequiredRequest(form);
    }

    private ReceivedAwardsRequestDto parseRequest(String data) {
        try {
            ReceivedAwardsRequestDto requestDto = objectMapper.readValue(data, ReceivedAwardsRequestDto.class);
            if (requestDto.name() == null || requestDto.name().trim().isEmpty()) {
                throw new BadRequestException("O nome do prêmio não pode estar vazio.");
            }
            return requestDto;
        } catch (JsonProcessingException e) {
            LOG.warnf("JSON inválido no campo 'data': %s", e.getMessage());
            throw new BadRequestException("Dados inválidos no campo 'data': " + e.getMessage());
        }
    }

    private String saveAwardImage(UUID awardId, FileUpload upload) {
        try {
            byte[] imageBytes = Files.readAllBytes(upload.filePath());
            String extension = extractExtension(upload.fileName());
            String fileName = awardId + extension;
            return storageService.uploadReceivedAward(imageBytes, fileName);
        } catch (IOException e) {
            LOG.errorf("Erro ao salvar imagem do prêmio recebido: %s", e.getMessage());
            throw new ApplicationException("Erro ao salvar imagem do prêmio recebido: " + e.getMessage());
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
