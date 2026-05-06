package br.com.avsistems.entity;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.dto.request.UserUpdateAdressDto;
import br.com.avsistems.dto.request.UserUpdateDto;
import br.com.avsistems.type.UserType;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@UserDefinition
public class UserEntity extends BaseEntity {
    //usuario inicial
    @Column(nullable=false, length = 100)
    public String name;

    @Column(nullable=false, unique=true, length = 100)
    @Username
    public String email;

    @Column(nullable=false, length = 255)
    @Password
    public String password;

    @Column(name="date_of_birth")
    public LocalDate dateOfBirth;

    @Column(name="instagram", length = 100)
    public String instagram;

    @Column(name="educational_instituition", length = 100)
    public String educationalInstituition;

    @Column(name="how_people_live_with_you")
    public Integer howPeopleLiveWithYou;

    @Column(name="user_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public UserType userType = UserType.USER;

    @Roles // O Quarkus vai ler as permissões através deste método
    public String getRole() {
        return userType.name(); // Retorna "ADMIN", "USER", etc.
    }

    @Column(name="total_points")
    public Integer totalPoints;

    @Column(name="redeemable_points")
    public Integer redeemablePoints;

    @Column(name="url_image", length = 255)
    public String urlImage;

    public Boolean active;

    @ManyToOne
    @JoinColumn(name = "mentes_editions")
    public MentesEditionEntity mentesEdition;

    //endereço
    @Column(name="street", length = 255)
    public String street;
    @Column(name="number", length = 10)
    public String number;
    @Column(name="neigborhood", length = 50)
    public String neigborhood;
    @Column(name="city", length = 50)
    public String city;
    @Column(name="state", length = 2)
    public String state;
    @Column(name="complement", length = 100)
    public String complement;
    @Column(name="zip_code", length = 9)
    public String zipCode;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    public Set<CompletedModulesEntity> completedModules = new HashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    public Set<ReceivedAwardsEntity> receivedAwards = new HashSet<>();


    public UserEntity(UserCreateDto userCreateDto) {
        this.name = userCreateDto.name();
        this.email = userCreateDto.email();
        this.password = BcryptUtil.bcryptHash(userCreateDto.password()); ///criptografa o password
        this.userType = UserType.USER;
        this.totalPoints = 0;
        this.redeemablePoints = 0;
        this.active = false;
    }

    public void updateUser(UserUpdateDto userUpdateDto, MentesEditionEntity edition) {
        this.name = userUpdateDto.name();
        this.email = userUpdateDto.email();
        this.dateOfBirth = userUpdateDto.dateOfBirth();
        this.instagram = userUpdateDto.instagram();
        this.educationalInstituition = userUpdateDto.educationalInstituition();
        this.mentesEdition = edition;
        this.howPeopleLiveWithYou = userUpdateDto.howPeopleLiveWithYou();
    }

    public void updateAddress(UserUpdateAdressDto userUpdateAdressDto) {
        this.street = userUpdateAdressDto.street();
        this.number = userUpdateAdressDto.number();
        this.neigborhood = userUpdateAdressDto.neigborhood();
        this.city = userUpdateAdressDto.city();
        this.state = userUpdateAdressDto.state();
        this.complement = userUpdateAdressDto.complement();
        this.zipCode = userUpdateAdressDto.zipCode();
    }

    public UserEntity() {

    }
}
