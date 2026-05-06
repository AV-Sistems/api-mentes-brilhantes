package br.com.avsistems.entity;

import br.com.avsistems.dto.request.MentesEditionRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "mentes_edition")
@SQLDelete(sql = "UPDATE mentes_edition SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class MentesEditionEntity extends BaseEntity{
    @Column(nullable = false, length = 100)
    public String title;

    @Column(name="date_edition", nullable = false)
    public LocalDate dateEdition;

    @Column(name="zip_code", nullable = false, length = 9)
    public String zipCode;
    @Column(nullable = false, length = 50)
    public String city;

    @Column(nullable = false, length = 2)
    public String state;

    public MentesEditionEntity() {}

    public MentesEditionEntity(MentesEditionRequestDto dto){
        this.title = dto.title();
        this.dateEdition = dto.dateEdition();
        this.zipCode = dto.zipCode();
        this.city = dto.city();
        this.state = dto.state();
    }
}
