package br.com.avsistems.service;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.entity.GiftsEntity;
import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.repository.GiftsRepository;
import br.com.avsistems.repository.TaskRepository;
import br.com.avsistems.type.TasksStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    GiftsRepository giftsRepository;

    public List<TaskResponseDto> listAllTasks(){
        return taskRepository.listAll().stream().map(TaskResponseDto::new).toList();
    }

    public List<TaskResponseDto> findByName(String name){
        return taskRepository.findByName(name).stream().map(TaskResponseDto::new).toList();
    }

    /** For admin: returns all active tasks regardless of gifts stock. */
    public List<TaskResponseDto> findByStatusActive(){
        return taskRepository.findByStatusActive().stream().map(TaskResponseDto::new).toList();
    }

    /** For users: returns only active tasks whose linked gifts (if any) still has stock. */
    public List<TaskResponseDto> findByStatusActiveForUser(){
        return taskRepository.findByStatusActiveForUser().stream().map(TaskResponseDto::new).toList();
    }

    public TaskResponseDto findById(UUID id){
        return taskRepository.findByIdOptional(id)
                .map(TaskResponseDto::new)
                .orElseThrow(() -> new ApplicationException("Tarefa com ID: " + id + " não encontrada."));
    }

    public List<TaskResponseDto> findByType(String type){
        return taskRepository.findByType(type).stream().map(TaskResponseDto::new).toList();
    }

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto dto) {
        validateRecurrenceDays(dto.recurrenceDays());
        TasksEntity task = new TasksEntity(dto);
        if (dto.giftsId() != null) {
            GiftsEntity gifts = giftsRepository.findByIdOptional(dto.giftsId())
                    .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));
            task.gifts = gifts;
        }
        taskRepository.persist(task);
        return new TaskResponseDto(task);
    }

    @Transactional
    public TaskResponseDto updateTask(UUID id, TaskRequestDto dto) {
        TasksEntity task = taskRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Tarefa não encontrada"));

            validateRecurrenceDays(dto.recurrenceDays());
            if (dto.name() != null) task.name = dto.name();
            if (dto.tasksPoints() != null) task.tasksPoints = dto.tasksPoints();
            if (dto.tasksStatus() != null) task.tasksStatus = dto.tasksStatus();
            if (dto.tasksType() != null) task.tasksType = dto.tasksType();
            if (dto.recurrenceDays() != null) task.recurrenceDays = dto.recurrenceDays();
            if (dto.giftsId() != null) {
                GiftsEntity gifts = giftsRepository.findByIdOptional(dto.giftsId())
                        .orElseThrow(() -> new NotFoundException("Brinde não encontrado."));
                task.gifts = gifts;
            }


        return new TaskResponseDto(task);
    }

    @Transactional
    public TaskResponseDto alterStatusTask(UUID id){
        TasksEntity task = taskRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Tarefa não encontrada"));
        if (task.tasksStatus == TasksStatus.INACTIVE) task.tasksStatus = TasksStatus.ACTIVE;
        else task.tasksStatus = TasksStatus.INACTIVE;
        return new TaskResponseDto(task);
    }

    @Transactional
    public void deleteTask(UUID id){
        TasksEntity task = taskRepository.findByIdOptional(id)
                .orElseThrow(() -> new ApplicationException("Tarefa não encontrada"));
        taskRepository.deleteById(task.id);
    }

    private void validateRecurrenceDays(Integer recurrenceDays) {
        if (recurrenceDays != null && recurrenceDays < 0) {
            throw new BadRequestException("O campo recurrenceDays não pode ser negativo.");
        }
    }
}
