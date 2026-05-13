package br.com.avsistems.entity;

import br.com.avsistems.config.AppTime;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_completed_modules")
@SQLDelete(sql = "UPDATE user_completed_modules SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserCompletedModulesEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "completed_module_id", nullable = false)
    public CompletedModulesEntity completedModule;

    @Column(name = "completed_date")
    public LocalDateTime completedDate;

    public UserCompletedModulesEntity() {
    }

    public UserCompletedModulesEntity(UserEntity user, CompletedModulesEntity completedModule) {
        this.user = user;
        this.completedModule = completedModule;
        this.completedDate = AppTime.now();
    }
}
