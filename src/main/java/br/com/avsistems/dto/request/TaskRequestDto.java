package br.com.avsistems.dto.request;

import br.com.avsistems.type.TasksStatus;
import br.com.avsistems.type.TasksType;

import java.util.UUID;

public record TaskRequestDto (
        String name,
        Integer tasksPoints,
        TasksStatus tasksStatus,
        TasksType tasksType,
        UUID giftsId,
        Integer recurrenceDays
){
}
