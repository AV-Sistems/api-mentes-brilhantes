package br.com.avsistems.repository;

import br.com.avsistems.entity.ReceivedAwardsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ReceivedAwardsRepository implements PanacheRepositoryBase<ReceivedAwardsEntity, UUID> {

    public List<ReceivedAwardsEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }
}

