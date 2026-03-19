package br.com.avsistems.service;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.exceptions.TaskExceptions;
import br.com.avsistems.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    public List<TaskResponseDto> listAllTasks(){
        return taskRepository.listAll().stream().map(TaskResponseDto::new).toList();
    }

    public TaskResponseDto findByName(String name){
        return taskRepository
                .findByName(name)
                .map(TaskResponseDto::new)
                .orElseThrow(()-> new TaskExceptions("Tarefa com nome: "+name+" não encontrada."));
    }

    public TaskResponseDto findById(UUID id){
        return taskRepository.findByIdOptional(id)
                .map(TaskResponseDto::new)
                .orElseThrow(()-> new TaskExceptions("Tarefa com ID: "+id+" não encontrada."));
    }

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto){
        TasksEntity task = new TasksEntity(taskRequestDto);
        taskRepository.persist(task);
        return new TaskResponseDto(task);
    }

    @Transactional
    public TaskResponseDto updateTask(UUID id, TaskRequestDto taskRequestDto){
        TasksEntity task = taskRepository.findByIdOptional(id)
                .orElseThrow(()-> new TaskExceptions("Tarefa não encontrada"));
        if(taskRequestDto.name() != null) task.name = taskRequestDto.name();
        if(taskRequestDto.description() != null)task.description = taskRequestDto.description();
        if(taskRequestDto.tasksPoints() != null)task.tasksPoints = taskRequestDto.tasksPoints();
        if(taskRequestDto.tasksStatus() != null)task.tasksStatus = taskRequestDto.tasksStatus();
        return new TaskResponseDto(task);
    }

    @Transactional
    public void deleteTask(UUID id){
        TasksEntity task = taskRepository.findByIdOptional(id)
                .orElseThrow(()-> new TaskExceptions("Tarefa não encontrada"));
        taskRepository.deleteById(task.id);
    }

}
