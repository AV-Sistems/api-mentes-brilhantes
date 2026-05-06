package br.com.avsistems.repository;

import br.com.avsistems.entity.TaskUserCompletedEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskUserCompletedRepository implements PanacheRepositoryBase<TaskUserCompletedEntity, UUID> {

    public List<TaskUserCompletedEntity> findByUserId(UUID userId) {
        return list("userEntity.id = ?1 order by createdAt desc", userId);
    }

    public List<TaskUserCompletedEntity> findByTaskId(UUID taskId) {
        return list("tasksEntity.id = ?1 order by createdAt desc", taskId);
    }

    public List<TaskUserCompletedEntity> findByVerified(Boolean verified) {
        return list("verified = ?1 order by createdAt desc", verified);
    }

    public List<TaskUserCompletedEntity> findByUserIdAndVerified(UUID userId, Boolean verified) {
        return list("userEntity.id = ?1 and verified = ?2 order by createdAt desc", userId, verified);
    }

    public List<TaskUserCompletedEntity> findByTaskIdAndVerified(UUID taskId, Boolean verified) {
        return list("tasksEntity.id = ?1 and verified = ?2 order by createdAt desc", taskId, verified);
    }

    public Optional<TaskUserCompletedEntity> findLastByUserAndTask(UUID userId, UUID taskId) {
        return find("userEntity.id = ?1 and tasksEntity.id = ?2 order by createdAt desc", userId, taskId)
                .firstResultOptional();
    }
}
