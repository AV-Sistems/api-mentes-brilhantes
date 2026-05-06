package br.com.avsistems.entity;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.type.TasksStatus;
import br.com.avsistems.type.TasksType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class TasksEntity extends BaseEntity{

    @Column(nullable=false)
    public String name;

    @Column(name="tasks_status", length = 20)
    @Enumerated(EnumType.STRING)
    public TasksStatus tasksStatus;

    @Column(name="tasks_points")
    public Integer tasksPoints;

    @ManyToOne
    @JoinColumn(name = "gifts_id")
    public GiftsEntity gifts;

    @Column(name="tasks_type", length = 20)
    @Enumerated(EnumType.STRING)
    public TasksType tasksType;

    @Column(name = "recurrence_days", nullable = false)
    public Integer recurrenceDays;

    public TasksEntity() {}

    public TasksEntity(TaskRequestDto taskRequestDto) {
        this.name = taskRequestDto.name();
        this.tasksPoints = taskRequestDto.tasksPoints();
        this.tasksStatus = taskRequestDto.tasksStatus();
        this.tasksType = taskRequestDto.tasksType();
        this.recurrenceDays = taskRequestDto.recurrenceDays() != null ? taskRequestDto.recurrenceDays() : 30;
    }

}
