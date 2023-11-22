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
public class Next_Of_Kin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "next_Of_KinID", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private String full_name;
    @Column(nullable = true)
    private String id_number;
    @Column(nullable = true)
    private String phone_number;
    @Column(nullable = false)
    private String user_signature;

    @Column(nullable = true)
    private String full_name2;
    @Column(nullable = true)
    private String id_number2;
    @Column(nullable = true)
    private String phone_number2;
}