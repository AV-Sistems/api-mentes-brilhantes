package br.com.avsistems.entity;

import br.com.avsistems.type.GiftsStatus;
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
@Table(name = "gifts")
@SQLDelete(sql = "UPDATE gifts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class GiftsEntity extends BaseEntity {

    @Column(nullable = false, length = 255)
    public String name;

    @Column(name = "points_cost", nullable = false)
    public Integer pointsCost;

    @Column(name = "is_advanced", nullable = false)
    public Boolean isAdvanced = Boolean.FALSE;

    @Column(nullable = false)
    public Integer stock;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public GiftsStatus status = GiftsStatus.ACTIVE;

    @Column(name = "requeriments", length = 255)
    public String requeriments;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    public PartnersEntity partner;

    public GiftsEntity() {
    }
}

