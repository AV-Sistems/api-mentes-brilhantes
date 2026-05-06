package br.com.avsistems.dto.response;

import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.type.TasksStatus;
import br.com.avsistems.type.TasksType;

import java.util.UUID;

public record TaskResponseDto
        (
                UUID id,
                String name,
                Integer tasksPoints,
                TasksStatus tasksStatus,
                GiftsResponseDto gifts,
                TasksType tasksType,
                Integer recurrenceDays

        ){
    public TaskResponseDto(TasksEntity tasksEntity){
        this(
                tasksEntity.id,
                tasksEntity.name,
                tasksEntity.tasksPoints,
                tasksEntity.tasksStatus,
                tasksEntity.gifts != null ? new GiftsResponseDto(tasksEntity.gifts) : null,
                tasksEntity.tasksType,
                tasksEntity.recurrenceDays
        );
    }
}
