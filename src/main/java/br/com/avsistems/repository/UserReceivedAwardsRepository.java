package br.com.avsistems.repository;

import br.com.avsistems.entity.UserReceivedAwardsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserReceivedAwardsRepository implements PanacheRepositoryBase<UserReceivedAwardsEntity, UUID> {

    public List<UserReceivedAwardsEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }

    public List<UserReceivedAwardsEntity> findByReceivedAwardId(UUID receivedAwardId) {
        return list("receivedAward.id = ?1", receivedAwardId);
    }

    public Optional<UserReceivedAwardsEntity> findByUserAndAward(UUID userId, UUID receivedAwardId) {
        return find("user.id = ?1 and receivedAward.id = ?2", userId, receivedAwardId).firstResultOptional();
    }
}

