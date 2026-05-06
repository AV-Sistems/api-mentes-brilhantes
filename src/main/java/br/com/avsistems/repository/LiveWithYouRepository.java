package br.com.avsistems.repository;

import br.com.avsistems.entity.LiveWithYouEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LiveWithYouRepository implements PanacheRepositoryBase<LiveWithYouEntity, UUID> {
    public List<LiveWithYouEntity> findByUserId(UUID userId) {
        return list("user.id", userId);
    }
}
