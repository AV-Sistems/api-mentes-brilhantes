package br.com.avsistems.repository;

import br.com.avsistems.entity.PartnersEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PartnersRepository implements PanacheRepositoryBase<PartnersEntity, UUID> {

    public List<PartnersEntity> findByValidatyAndStateAndCity(LocalDate today, String state, String city) {
        return list("validity >=?1 and lower(state) = ?2 and lower(city) = ?3",today, state, city);
    }

    public List<PartnersEntity> findByValidity(LocalDate today) {
        return list("validity >=?1",today);
    }
}
