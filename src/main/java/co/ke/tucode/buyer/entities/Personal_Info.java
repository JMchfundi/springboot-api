package co.ke.tucode.buyer.entities;

import java.util.Date;

import jakarta.persistence.*;

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
public class Personal_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personal_InfoID", updatable = false, nullable = false)
    private Integer id;

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
    @Column(nullable = false)
    private String user_signature;
}
