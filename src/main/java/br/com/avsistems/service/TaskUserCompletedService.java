package br.com.avsistems.service;

import br.com.avsistems.dto.request.TaskUserCompletedRequestDto;
import br.com.avsistems.dto.response.TaskUserCompletedResponseDto;
import br.com.avsistems.entity.TaskUserCompletedEntity;
import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
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

    public TaskUserCompletedResponseDto findById(UUID id) {
        return repository.findByIdOptional(id)
                .map(TaskUserCompletedResponseDto::new)
                .orElseThrow(() -> new NotFoundException("Conclusão de tarefa não encontrada."));
    }

    public List<TaskUserCompletedResponseDto> listByUser(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    public List<TaskUserCompletedResponseDto> listByTask(UUID taskId) {
        return repository.findByTaskId(taskId).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    public List<TaskUserCompletedResponseDto> listVerified() {
        return repository.findByVerified(Boolean.TRUE).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    public List<TaskUserCompletedResponseDto> listPending() {
        return repository.findByVerified(Boolean.FALSE).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    public List<TaskUserCompletedResponseDto> listByUserAndVerified(UUID userId, boolean verified) {
        return repository.findByUserIdAndVerified(userId, verified).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    public List<TaskUserCompletedResponseDto> listByTaskAndVerified(UUID taskId, boolean verified) {
        return repository.findByTaskIdAndVerified(taskId, verified).stream()
                .map(TaskUserCompletedResponseDto::new)
                .toList();
    }

    @Transactional
    public TaskUserCompletedResponseDto createCompletedTask(TaskUserCompletedRequestDto dto) {

        if (dto.userId() == null || dto.taskId() == null) {
            throw new BadRequestException("taskId e userId sao obrigatorios.");
        }

        UserEntity user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        TasksEntity task = taskRepository.findByIdOptional(dto.taskId())
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));

        Integer recurrenceDays = task.recurrenceDays != null ? task.recurrenceDays : 1;
        repository.findLastByUserAndTask(user.id, task.id);

        TaskUserCompletedEntity completed = new TaskUserCompletedEntity();
        completed.userEntity = user;
        completed.tasksEntity = task;
        completed.verified = false;

        repository.persist(completed);

        return new TaskUserCompletedResponseDto(completed);
    }

    //função pra modificar o task como concluido
    @Transactional
    public TaskUserCompletedResponseDto verifyTaskCompleted(UUID id) {
        TaskUserCompletedEntity completed = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Conclusão de tarefa não encontrada."));

        if (Boolean.TRUE.equals(completed.verified)) {
            throw new BadRequestException("Essa conclusão de tarefa já foi verificada.");
        }

        completed.verified = true;
        TasksEntity task = completed.tasksEntity;
        UserEntity user = completed.userEntity;

        //  LOGICA DE PONTOS: Acumula no usuário
        if (task.tasksPoints != null) {
            int currentTotal = user.totalPoints == null ? 0 : user.totalPoints;
            int currentRedeemable = user.redeemablePoints == null ? 0 : user.redeemablePoints;
            user.totalPoints = currentTotal + task.tasksPoints;
            user.redeemablePoints = currentRedeemable + task.tasksPoints;
        }

        repository.persist(completed);

        return new TaskUserCompletedResponseDto(completed);
    }

    @Transactional
    public void deleteCompletedTask(UUID id) {
        TaskUserCompletedEntity completed = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Conclusão de tarefa não encontrada."));

        if (Boolean.TRUE.equals(completed.verified) && completed.tasksEntity.tasksPoints != null) {
            UserEntity user = completed.userEntity;
            int currentTotal = user.totalPoints != null ? user.totalPoints : 0;
            int currentRedeemable = user.redeemablePoints != null ? user.redeemablePoints : 0;
            user.totalPoints = Math.max(0, currentTotal - completed.tasksEntity.tasksPoints);
            user.redeemablePoints = Math.max(0, currentRedeemable - completed.tasksEntity.tasksPoints);
        }

        repository.delete(completed);
    }
}