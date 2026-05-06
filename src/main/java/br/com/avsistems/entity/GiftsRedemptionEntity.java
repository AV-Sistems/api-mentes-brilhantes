package br.com.avsistems.entity;

import br.com.avsistems.type.GiftsRedemptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "gifts_redemptions")
@SQLDelete(sql = "UPDATE gifts_redemptions SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class GiftsRedemptionEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "gifts_id", nullable = false)
    public GiftsEntity gifts;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public GiftsRedemptionStatus status = GiftsRedemptionStatus.PENDING;

    @Column(name = "points_used", nullable = false)
    public Integer pointsUsed;

    public GiftsRedemptionEntity() {
    }
}

