package br.com.avsistems.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "received_awards")
@SQLDelete(sql = "UPDATE received_awards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ReceivedAwardsEntity extends BaseEntity {

    @Column(nullable = false, length = 255)
    public String name;

    @Column(name = "image_url", length = 255)
    public String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "user_received_awards",
            joinColumns = @JoinColumn(name = "received_award_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<UserEntity> users = new HashSet<>();

    public ReceivedAwardsEntity() {
    }

    public ReceivedAwardsEntity(String name) {
        this.name = name;
    }

    public ReceivedAwardsEntity(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
