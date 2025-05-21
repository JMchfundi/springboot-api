package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.ke.tucode.systemuser.entities.Africana_User;

@Entity
@Table(name = "officer_registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Step 1: Officer details
    private String fullName;
    private String email;
    private String phoneNumber;
    private String idNumber;
    private String dob;
    private String gender;
    private String branchLocation;

    // Step 2: Emergency contact
    private String nokName;
    private String nokPhone;
    private String nokRelationship;

    // Step 3: Bank details
    private String bankDetails;

    // File names or paths
    private String idDocumentPath;
    private String passportPhotoPath;

    @OneToOne(mappedBy = "officer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Africana_User systemUser;
}
