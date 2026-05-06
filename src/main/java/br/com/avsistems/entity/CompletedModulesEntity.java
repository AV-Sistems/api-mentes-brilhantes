package br.com.avsistems.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "completed_modules")
@SQLDelete(sql = "UPDATE completed_modules SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CompletedModulesEntity extends BaseEntity {

    @Column(nullable = false, length = 255)
    public String name;

    @ManyToMany
    @JoinTable(
            name = "user_completed_modules",
            joinColumns = @JoinColumn(name = "completed_module_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<UserEntity> users = new HashSet<>();

    public CompletedModulesEntity() {
    }

    public CompletedModulesEntity(String name) {
        this.name = name;
    }
}

