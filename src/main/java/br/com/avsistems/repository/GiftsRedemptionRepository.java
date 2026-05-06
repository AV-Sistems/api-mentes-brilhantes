package br.com.avsistems.repository;

import br.com.avsistems.entity.GiftsRedemptionEntity;
import br.com.avsistems.type.GiftsRedemptionStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GiftsRedemptionRepository implements PanacheRepositoryBase<GiftsRedemptionEntity, UUID> {

    public List<GiftsRedemptionEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }

    public List<GiftsRedemptionEntity> findByStatus(GiftsRedemptionStatus status) {
        return list("status = ?1", status);
    }
}

