package br.com.avsistems.repository;

import br.com.avsistems.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped // Faz o Quarkus gerenciar esta classe (Injeção de Dependência)
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {
    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
