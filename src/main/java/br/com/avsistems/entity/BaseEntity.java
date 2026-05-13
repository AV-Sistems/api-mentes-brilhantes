package br.com.avsistems.entity;

import br.com.avsistems.config.AppTime;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity extends io.quarkus.hibernate.orm.panache.PanacheEntityBase{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name="id", updatable = false, nullable = false)
    public UUID id;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = AppTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = AppTime.now();
    }

    // Método auxiliar para o Soft Delete
    public void softDelete() {
        this.deletedAt = AppTime.now();
        this.persist();
    }

}
