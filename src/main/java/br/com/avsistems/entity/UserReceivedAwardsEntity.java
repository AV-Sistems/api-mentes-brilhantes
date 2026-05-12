package br.com.avsistems.entity;

import br.com.avsistems.config.AppTime;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_received_awards")
@SQLDelete(sql = "UPDATE user_received_awards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserReceivedAwardsEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "received_award_id", nullable = false)
    public ReceivedAwardsEntity receivedAward;

    @Column(name = "awarded_date")
    public LocalDateTime awardedDate;

    public UserReceivedAwardsEntity() {
    }

    public UserReceivedAwardsEntity(UserEntity user, ReceivedAwardsEntity receivedAward) {
        this.user = user;
        this.receivedAward = receivedAward;
        this.awardedDate = AppTime.now();
    }
}
