package br.com.avsistems.service;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.dto.request.UserImageMultipartForm;
import br.com.avsistems.dto.request.UserUpdateAdressDto;
import br.com.avsistems.dto.request.UserUpdateDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.entity.CompletedModulesEntity;
import br.com.avsistems.entity.MentesEditionEntity;
import br.com.avsistems.entity.ReceivedAwardsEntity;
import br.com.avsistems.entity.UserCompletedModulesEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.entity.UserReceivedAwardsEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.repository.CompletedModulesRepository;
import br.com.avsistems.repository.MentesEditionRepository;
import br.com.avsistems.repository.ReceivedAwardsRepository;
import br.com.avsistems.repository.UserCompletedModulesRepository;
import br.com.avsistems.repository.UserReceivedAwardsRepository;
import br.com.avsistems.repository.UserRepository;
import br.com.avsistems.type.UserType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    MentesEditionRepository editionRepository;

    @Inject
    FileStorageService storageService;

    @Inject
    CompletedModulesRepository completedModulesRepository;

    @Inject
    ReceivedAwardsRepository receivedAwardsRepository;

    @Inject
    UserCompletedModulesRepository userCompletedModulesRepository;

    @Inject
    UserReceivedAwardsRepository userReceivedAwardsRepository;

    public List<UserResponseDto> findAllInactiveUser(){
        return userRepository.listAllInactiveUser().stream().map(UserResponseDto::new).toList();
    }
    public List<UserResponseDto> findAllActiveUser(){
        return userRepository.listAllActiveUser().stream().map(UserResponseDto::new).toList();
    }

    public List<UserResponseDto> findRankingByTotalPoints(){
        return userRepository.listRankingByTotalPoints().stream().map(UserResponseDto::new).toList();
    }

    public List<UserResponseDto> findByNameContaining(String name){
        return userRepository.findByNameContaining(name.toLowerCase()).stream().map(UserResponseDto::new).toList();
    }

    public List<UserResponseDto> findByStateAndCity(String state, String city){
        return userRepository.listUserAllStateAndCity(state.toLowerCase(), city.toLowerCase())
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }
    public List<UserResponseDto> findByEducationInstituitionContaining(String instituition) {
        return userRepository.findByEducationInstituitionContaining(instituition)
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public List<UserResponseDto> findByUserTypeContaining(String userType) {
        return userRepository.findByUserTypeContaining(userType)
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }
    public List<UserResponseDto> findByMentesEditionContaining(String edition) {
        return userRepository.findByMentesEditionContaining(edition)
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }
    public List<UserResponseDto> findByDateOfBirth(LocalDate date) {
        return userRepository.findByDateOfBirth(date)
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public UserResponseDto findById(UUID id){
        if(userRepository.findByIdOptional(id).isEmpty()){
            throw new ApplicationException("Usuário não encontrado.");
        }
        return new UserResponseDto(userRepository.findById(id));
    }

    @Transactional //abre a transação com o banco de dados
    public UserResponseDto createUser(UserCreateDto userCreateDto){
        if(userRepository.findByEmail(userCreateDto.email()).isPresent()){
            throw new ApplicationException("Email já cadastrado.");
        }
        UserEntity userEntity = new UserEntity(userCreateDto);

        if (userCreateDto.mentesEditionId() != null) {
            MentesEditionEntity edition = editionRepository.findById(userCreateDto.mentesEditionId());
            if (edition == null) throw new ApplicationException("Edição não encontrada.");
            userEntity.mentesEdition = edition;
        }

        userRepository.persist(userEntity);
        persistCompletedModules(userEntity, userCreateDto.completedModuleIds());
        persistReceivedAwards(userEntity, userCreateDto.receivedAwardIds());
        return new UserResponseDto(userEntity);
    }

    @Transactional
    public UserResponseDto updateUser(UUID id, UserUpdateDto userUpdateDto){
        UserEntity user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));

        // Busca a edição se o ID foi enviado
        MentesEditionEntity edition = null;
        if (userUpdateDto.mentesEditionId() != null) {
            edition = editionRepository.findById(userUpdateDto.mentesEditionId());
            if (edition == null) throw new ApplicationException("Edição não encontrada.");
        }

        user.updateUser(userUpdateDto, edition);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUserAddress(UUID id, UserUpdateAdressDto userUpdateAdressDto){
        UserEntity user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));
        user.updateAddress(userUpdateAdressDto);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto addUserImage(UUID id, UserImageMultipartForm form){
        System.out.println(form);
        try {
            UserEntity user = userRepository.findByIdOptional(id)
                    .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));

            byte[] imageBytes = Files.readAllBytes(form.file.filePath());

            String extension = form.file.fileName().substring(form.file.fileName().lastIndexOf("."));
            String fileName = user.id + extension;

            String path = storageService.uploadUserImage(imageBytes, fileName);

            user.urlImage = path;

            userRepository.persist(user);

            return new UserResponseDto(user);
        }catch (Exception e){
            throw new ApplicationException("Erro ao adicionar imagem: "+e);
        }
    }

    @Transactional
    public UserResponseDto alterActivation(UUID id){
        UserEntity user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));

        user.active = !user.active;
        userRepository.persist(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto toggleUserType(UUID id, UserType userType) {
        UserEntity user = userRepository.findByIdOptional(id)
                .orElseThrow(()-> new ApplicationException("Usuário não encontrado."));

        user.userType = userType;
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(UUID id){
        UserEntity user = userRepository.findByIdOptional(id)
                        .orElseThrow(() -> new ApplicationException("Usuário não encontrado."));
        userRepository.deleteById(user.id);
    }

    private void persistCompletedModules(UserEntity userEntity, List<UUID> moduleIds) {
        LinkedHashSet<UUID> uniqueModuleIds = normalizeSelectionIds(
                moduleIds,
                "A lista de módulos concluídos contém itens inválidos."
        );

        for (UUID moduleId : uniqueModuleIds) {
            CompletedModulesEntity module = completedModulesRepository.findByIdOptional(moduleId)
                    .orElseThrow(() -> new ApplicationException("Módulo com ID: " + moduleId + " não encontrado."));
            userCompletedModulesRepository.persist(new UserCompletedModulesEntity(userEntity, module));
        }
    }

    private void persistReceivedAwards(UserEntity userEntity, List<UUID> receivedAwardIds) {
        LinkedHashSet<UUID> uniqueAwardIds = normalizeSelectionIds(
                receivedAwardIds,
                "A lista de prêmios recebidos contém itens inválidos."
        );

        for (UUID receivedAwardId : uniqueAwardIds) {
            ReceivedAwardsEntity award = receivedAwardsRepository.findByIdOptional(receivedAwardId)
                    .orElseThrow(() -> new ApplicationException("Prêmio com ID: " + receivedAwardId + " não encontrado."));
            userReceivedAwardsRepository.persist(new UserReceivedAwardsEntity(userEntity, award));
        }
    }

    private LinkedHashSet<UUID> normalizeSelectionIds(List<UUID> ids, String invalidListMessage) {
        if (ids == null || ids.isEmpty()) {
            return new LinkedHashSet<>();
        }

        if (ids.stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException(invalidListMessage);
        }

        return new LinkedHashSet<>(ids);
    }
}
