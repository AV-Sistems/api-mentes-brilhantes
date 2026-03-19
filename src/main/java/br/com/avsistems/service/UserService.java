package br.com.avsistems.service;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.dto.request.UserUpdateDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.UserExceptions;
import br.com.avsistems.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional //abre a transação com o banco de dados
    public UserResponseDto createUser(UserCreateDto userCreateDto){
        if(userRepository.findByEmail(userCreateDto.email()).isPresent()){
            throw new UserExceptions("Email já cadastrado.");
        }
        UserEntity userEntity = new UserEntity(userCreateDto);
        userRepository.persist(userEntity);
        return new UserResponseDto(userEntity);
    }

    public List<UserResponseDto> listAllUsers(){
        return userRepository.listAll().stream().map(UserResponseDto::new).toList();
    }

    public UserResponseDto findById(UUID id){
        return userRepository.findByIdOptional(id)
                .map(UserResponseDto::new)
                .orElseThrow(() -> new UserExceptions("Usuário não encontrado."));
    }

    public UserResponseDto findByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserResponseDto::new)
                .orElseThrow(()-> new UserExceptions("Usuário não encontrado."));
    }

    @Transactional
    public UserResponseDto updateUser(UUID id, UserUpdateDto userUpdateDto){
        UserEntity userEntity = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new UserExceptions("Usuário não encontrado."));

        if (userUpdateDto.email() != null && !userUpdateDto.email().equals(userEntity.email)) {
            if (userRepository.findByEmail(userUpdateDto.email()).isPresent()) {
                throw new UserExceptions("Este e-mail já está em uso por outro usuário.");
            }
            userEntity.email = userUpdateDto.email();
        }

        if(userUpdateDto.name() != null) userEntity.name = userUpdateDto.name();
        if(userUpdateDto.userType() != null)userEntity.userType = userUpdateDto.userType();

        return new UserResponseDto(userEntity);
    }

    @Transactional
    public void deleteUser(UUID id){
        UserEntity user = userRepository.findByIdOptional(id)
                        .orElseThrow(() -> new UserExceptions("Usuário não encontrado."));
        userRepository.deleteById(user.id);
    }
}
