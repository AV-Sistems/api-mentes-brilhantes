package br.com.avsistems.entity;

import br.com.avsistems.dto.request.LiveWithYouDto;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "live_with_you")
@SQLDelete(sql = "UPDATE live_with_you SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class LiveWithYouEntity extends BaseEntity{

    @Column(nullable=false, length = 100)
    public String name;

    @Column(nullable=false, length = 50)
    public String relationshipType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @Column(length = 15)
    public String phone;

    @Column(name = "date_of_birth")
    public LocalDate dateOfBirth;

    public LiveWithYouEntity(LiveWithYouDto liveWithYouRequestDto, UserEntity user) {
        this.name = liveWithYouRequestDto.name();
        this.relationshipType = liveWithYouRequestDto.relationshipType();
        this.phone = liveWithYouRequestDto.phone();
        this.dateOfBirth = liveWithYouRequestDto.dateOfBirth();
        this.user  = user; }


    public LiveWithYouEntity() {}


}
