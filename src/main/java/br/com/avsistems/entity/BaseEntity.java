package br.com.avsistems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class BaseEntity extends io.quarkus.hibernate.orm.panache.PanacheEntityBase{
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Método auxiliar para o Soft Delete
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.persist();
    }

    public static class TaskUserCompletedEntity extends BaseEntity {
    }
}
