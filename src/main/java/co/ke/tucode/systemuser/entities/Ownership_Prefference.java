package co.ke.tucode.systemuser.entities;

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
public class Ownership_Prefference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ownership_PrefferenceID", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private String home_type;
    @Column(nullable = true)
    private String owning_reason;
    @Column(nullable = true)
    private String preffered_country;
    @Column(nullable = false)
    private String user_signature;
}