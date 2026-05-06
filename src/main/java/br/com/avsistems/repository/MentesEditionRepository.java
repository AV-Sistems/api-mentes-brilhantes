package br.com.avsistems.repository;

import br.com.avsistems.entity.MentesEditionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MentesEditionRepository implements PanacheRepositoryBase<MentesEditionEntity, UUID> {
    public Optional<MentesEditionEntity> findByTitle(String title) {
        return find("lower(title) = lower(?1)", title).firstResultOptional();
    }
    public List<MentesEditionEntity> findByStateAndCity(String state, String city) {
        return list("lower(state) = ?1 and lower(city) = ?2", state, city);
    }

}
