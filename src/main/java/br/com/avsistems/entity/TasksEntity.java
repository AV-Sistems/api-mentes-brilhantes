package br.com.avsistems.entity;


import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.type.TasksStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class TasksEntity extends BaseEntity{

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name="id", updatable = false, nullable = false)
    public UUID id;

    @Column(nullable=false)
    public String name;

    @Column(nullable=false)
    public String description;

    @Column(name="tasks_status")
    @Enumerated(EnumType.STRING)
    public TasksStatus tasksStatus;

    @Column(name="tasks_points", nullable = false)
    public Integer tasksPoints;

    public TasksEntity() {}

    public TasksEntity(TaskRequestDto taskRequestDto) {
        this.name = taskRequestDto.name();
        this.description = taskRequestDto.description();
        this.tasksPoints = taskRequestDto.tasksPoints();
        this.tasksStatus = taskRequestDto.tasksStatus();
    }

}
