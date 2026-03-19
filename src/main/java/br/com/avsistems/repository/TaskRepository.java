package br.com.avsistems.repository;

import br.com.avsistems.entity.TasksEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TasksEntity, UUID> {
    public Optional<TasksEntity> findByName(String name) {
        return find("lower(name) = lower(?1)", name).firstResultOptional();
    }
}
