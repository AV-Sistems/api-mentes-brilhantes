package br.com.avsistems.repository;

import br.com.avsistems.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped // Faz o Quarkus gerenciar esta classe (Injeção de Dependência)
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {
    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<UserEntity> listAllInactiveUser() {
        return list("active = ?1", false);
    }

    public List<UserEntity> listAllActiveUser() {
        return list("active = ?1", true);
    }

    public List<UserEntity> listRankingByTotalPoints() {
        return list("active = true order by totalPoints desc, name asc");
    }

    public List<UserEntity> listUserAllStateAndCity(String state, String city) {
        return list("lower(state) = ?1 and lower(city) = ?2 and active = true", state, city);
    }

    public List<UserEntity> findByNameContaining(String name) {
        return list("lower(name) like lower(?1) and active = true", "%"+(name)+"%");
    }

    public List<UserEntity> findByEducationInstituitionContaining(String instituition) {
        return list("lower(educational_instituition) like lower(?1) and active = true", "%"+(instituition)+"%");
    }

    public List<UserEntity> findByUserTypeContaining(String userType) {
        return list("lower(user_type) like lower(?1) and active = true", "%"+(userType)+"%");
    }

    public List<UserEntity> findByMentesEditionContaining(String edition) {
        return list("lower(mentes_edition) like lower(?1) and active = true", "%"+(edition)+"%");
    }
    public List<UserEntity> findByDateOfBirth(LocalDate date) {
        return list("date_of_birth = ?1 and active = true", date);
    }

    public long resetAllPoints() {
        return update("totalPoints = 0, redeemablePoints = 0");
    }
}
