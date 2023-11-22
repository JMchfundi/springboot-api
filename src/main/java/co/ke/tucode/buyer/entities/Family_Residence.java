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
public class Family_Residence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "residenceID", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private Integer children;
    @Column(nullable = true)
    private String current_residence;
    @Column(nullable = true)
    private Boolean live_with_family;
    @Column(nullable = true)
    private Integer rental;
    @Column(nullable = false)
    private String user_signature;
}