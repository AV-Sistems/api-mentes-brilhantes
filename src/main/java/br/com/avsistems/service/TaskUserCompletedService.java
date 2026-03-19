package br.com.avsistems.service;

import br.com.avsistems.dto.request.TaskUserCompletedRequestDto;
import br.com.avsistems.dto.response.TaskUserCompletedResponseDto;
import br.com.avsistems.entity.TaskUserCompletedEntity;
import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.TaskExceptions;
import br.com.avsistems.exceptions.UserExceptions;
import br.com.avsistems.repository.TaskRepository;
import br.com.avsistems.repository.TaskUserCompletedRepository;
import br.com.avsistems.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskUserCompletedService {

    @Inject
    TaskUserCompletedRepository repository;

    @Inject
    UserRepository userRepository;
    @Inject
    TaskRepository taskRepository;

    public List<TaskUserCompletedResponseDto> listAll() {
        return repository.listAll().stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    @Transactional
    public TaskUserCompletedResponseDto createCompletedTask(TaskUserCompletedRequestDto dto) {
        // Busca as entidades
        UserEntity user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new UserExceptions("Usuário não encontrado."));

        TasksEntity task = taskRepository.findByIdOptional(dto.taskId())
                .orElseThrow(() -> new TaskExceptions("Tarefa não encontrada."));

        // Cria o registro de conclusão
        TaskUserCompletedEntity completed = new TaskUserCompletedEntity();
        completed.userEntity = user;
        completed.tasksEntity = task;
        completed.verified = false;

        repository.persist(completed);

        return new TaskUserCompletedResponseDto(completed);
    }

    //função pra modificar o task como concluido
    @Transactional
    public TaskUserCompletedResponseDto updateTaskCompleted(UUID id) {
        TaskUserCompletedEntity completed = repository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada pelo id: "+id));

        completed.verified = true;
        TasksEntity task = completed.tasksEntity;
        UserEntity user = completed.userEntity;

        //  LOGICA DE PONTOS: Acumula no usuário
        if (task.tasksPoints != null) {
            user.totalPoints = (user.totalPoints == null ? 0 : user.totalPoints) + task.tasksPoints;
        }

        repository.persist(completed);

        return new TaskUserCompletedResponseDto(completed);
    }
}