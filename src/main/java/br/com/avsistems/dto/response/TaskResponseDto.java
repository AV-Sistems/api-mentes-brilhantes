package br.com.avsistems.dto.response;

import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.type.TasksStatus;

import java.util.UUID;

public record TaskResponseDto
        (
                UUID id,
                String name,
                String description,
                Integer tasksPoints,
                TasksStatus tasksStatus

        ){
    public TaskResponseDto(TasksEntity tasksEntity){
        this(
                tasksEntity.id,
                tasksEntity.name,
                tasksEntity.description,
                tasksEntity.tasksPoints,
                tasksEntity.tasksStatus
        );
    }
}
