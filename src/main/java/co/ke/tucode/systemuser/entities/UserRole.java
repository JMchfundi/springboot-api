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
@Table(name = "USER_ROLES")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_ID", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = true)
    private String role_name;
    @Column(nullable = true)
    private String role_description;
    @Column(nullable = false)
    private String user_signature;
}