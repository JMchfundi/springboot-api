package co.ke.tucode.systemuser.entities;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.lang.Nullable;

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
public class Back {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personal_InfoID", updatable = true)
    private Integer personal_InfoID;

    @Column(nullable = true)
    private String residence;
    @Column(nullable = true)
    private String state_city;
    @Column(nullable = true)
    private String phone_number;
    @Column(nullable = true)
    private String id_number;
    @Column(nullable = true)
    private String kra_pin;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private Date dob;
    @Column(nullable = true)
    private String marital_status;
    @Column(nullable = true)
    private Boolean disability;
}
