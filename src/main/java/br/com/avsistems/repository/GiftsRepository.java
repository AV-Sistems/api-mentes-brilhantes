package br.com.avsistems.repository;

import br.com.avsistems.entity.GiftsEntity;
import br.com.avsistems.type.GiftsStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GiftsRepository implements PanacheRepositoryBase<GiftsEntity, UUID> {

    public List<GiftsEntity> findAvailable() {
        return list("status = ?1 AND stock > 0", GiftsStatus.ACTIVE);
    }

    public List<GiftsEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }
}

