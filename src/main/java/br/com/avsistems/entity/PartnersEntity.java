package br.com.avsistems.entity;

import br.com.avsistems.dto.request.PartnersCreateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "partners")
@SQLDelete(sql = "UPDATE partners SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PartnersEntity extends BaseEntity{

    @Column(nullable=false, length = 100)
    public String name;

    @Column(name="image_url")
    public String imageUrl;

    public String url;

    public LocalDate validity;

    @Column(name="city", length = 50)
    public String city;
    @Column(name="state", length = 2)
    public String state;
    @Column(name="zip_code", length = 9)
    public String zipCode;

    public PartnersEntity() {}

    public PartnersEntity(PartnersCreateDto partnersCreateDto) {
        this.name = partnersCreateDto.name();
        this.url = partnersCreateDto.url();
        this.validity = partnersCreateDto.validity();
        this.city = partnersCreateDto.city();
        this.state = partnersCreateDto.state();
        this.zipCode = partnersCreateDto.zipCode();
    }

}
