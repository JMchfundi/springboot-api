package co.ke.tucode.buyer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
public class Africana_User_Back {

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

    @Column(unique = true, insertable = false, updatable = false)
    private Integer personal_InfoID;

    @OneToOne(cascade = CascadeType.ALL)
    //@MapsId
    @JoinColumn(name = "personal_InfoID")
    private Back personal_Info;
}
