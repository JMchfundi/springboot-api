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
public class CitizenCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoryId", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private Boolean civil_servant;
    @Column(nullable = true)
    private Boolean defence_force;
    @Column(nullable = true)
    private Boolean sacco_member;
    @Column(nullable = false)
    private String user_signature;
    // @Column(nullable = true)
    // private Boolean purchase_mode;
}