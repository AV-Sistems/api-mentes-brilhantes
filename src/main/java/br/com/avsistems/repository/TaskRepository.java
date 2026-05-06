package br.com.avsistems.repository;

import br.com.avsistems.entity.TasksEntity;
import br.com.avsistems.type.TasksStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TasksEntity, UUID> {
    public List<TasksEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%"+(name)+"%");
    }
    public List<TasksEntity> findByStatusActive() {
        return list("tasksStatus = ?1", TasksStatus.ACTIVE);
    }
    /** Active tasks visible to users: excludes tasks whose linked gifts is out of stock. */
    public List<TasksEntity> findByStatusActiveForUser() {
        return list("tasksStatus = ?1 AND (gifts IS NULL OR gifts.stock > 0)", TasksStatus.ACTIVE);
    }
    public List<TasksEntity> findByType(String type) {
        return list("tasks_type = ?1", type);
    }
}
