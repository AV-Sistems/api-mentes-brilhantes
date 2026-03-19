package br.com.avsistems.dto.request;

import br.com.avsistems.type.TasksStatus;

public record TaskRequestDto (
        String name,
        String description,
        Integer tasksPoints,
        TasksStatus tasksStatus
){
}
