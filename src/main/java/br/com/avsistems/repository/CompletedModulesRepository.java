package br.com.avsistems.repository;

import br.com.avsistems.entity.CompletedModulesEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CompletedModulesRepository implements PanacheRepositoryBase<CompletedModulesEntity, UUID> {

    public List<CompletedModulesEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }
}

