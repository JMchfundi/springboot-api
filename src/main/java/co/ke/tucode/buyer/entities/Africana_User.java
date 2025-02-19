package co.ke.tucode.buyer.entities;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import co.ke.tucode.buyer.entities.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Africana_User{

//    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @NotEmpty
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @NotEmpty
    private String password;
    @Column(nullable = true)
    private String access;
    @Column(nullable = false)
    private String user_signature;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer categoryId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private CitizenCategory citizenCategory;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer personal_InfoID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_InfoID")
    private Personal_Info personal_Info;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer residenceID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "residenceID")
    private Family_Residence residence;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer next_Of_KinID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_Of_KinID")
    private Next_Of_Kin next_Of_Kin;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer employment_DetailsID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_DetailsID")
    private Employment_Details employment_Details;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer ownership_PrefferenceID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ownership_PrefferenceID")
    private Ownership_Prefference ownership_Prefference;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer role_ID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_ID")
    private UserRole userRole;
}
