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
public class Employment_Details {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employment_DetailsID", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private String income_range;
    @Column(nullable = true)
    private String occupation_qualification;
    @Column(nullable = true)
    private String employement_status;
    @Column(nullable = true)
    private String describe_income;
    @Column(nullable = true)
    private String employement_period;
    @Column(nullable = true)
    private String current_employer_category;
    @Column(nullable = true)
    private String employment_number;
    @Column(nullable = false)
    private String user_signature;
}