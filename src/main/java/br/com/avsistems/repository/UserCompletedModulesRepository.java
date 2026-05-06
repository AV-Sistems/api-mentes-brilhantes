package br.com.avsistems.repository;

import br.com.avsistems.entity.UserCompletedModulesEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserCompletedModulesRepository implements PanacheRepositoryBase<UserCompletedModulesEntity, UUID> {

    public List<UserCompletedModulesEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }

    public List<UserCompletedModulesEntity> findByCompletedModuleId(UUID completedModuleId) {
        return list("completedModule.id = ?1", completedModuleId);
    }

    public Optional<UserCompletedModulesEntity> findByUserAndModule(UUID userId, UUID completedModuleId) {
        return find("user.id = ?1 and completedModule.id = ?2", userId, completedModuleId).firstResultOptional();
    }
}

