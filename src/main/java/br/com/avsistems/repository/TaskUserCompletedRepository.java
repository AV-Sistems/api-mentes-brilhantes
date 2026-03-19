package br.com.avsistems.repository;

import br.com.avsistems.entity.TaskUserCompletedEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class TaskUserCompletedRepository implements PanacheRepositoryBase<TaskUserCompletedEntity, UUID> {
}
