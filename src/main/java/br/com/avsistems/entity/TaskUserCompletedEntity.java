package br.com.avsistems.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "task_user_completed")
@SQLDelete(sql = "UPDATE task_user_completed SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class TaskUserCompletedEntity extends BaseEntity{

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name="id", updatable = false, nullable = false)
    public UUID id;

    @Column(nullable = false)
    public Boolean verified;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    public TasksEntity tasksEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity userEntity;

}
