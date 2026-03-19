package br.com.avsistems.entity;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.type.UserType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name="id", updatable = false, nullable = false)
    public UUID id;

    @Column(nullable=false)
    public String name;

    @Column(nullable=false, unique=true)
    public String email;

    @Column(nullable=false)
    public String password;

    @Column(name="user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public UserType userType;

    @Column(name="total_points")
    public Integer totalPoints;

    public UserEntity(UserCreateDto userCreateDto) {
        this.name = userCreateDto.name();
        this.email = userCreateDto.email();
        this.password = userCreateDto.password();
        this.userType = userCreateDto.userType();
    }

    public UserEntity() {

    }
}
