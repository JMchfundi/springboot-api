package co.ke.tucode.buyer.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.ke.tucode.buyer.repositories.UserRoleRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Africana_User implements UserDetails {

    // private static final long serialVersionUID = 1L;

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

    // @Column(unique = true, insertable = false, updatable = false)
    // private Integer role_ID;
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "role_ID")
    // private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
}
