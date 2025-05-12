package co.ke.finsis.entity;
import jakarta.persistence.*;
import lombok.*;

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

    // File names or paths (for simplicity)
    private String idDocumentPath;
    private String passportPhotoPath;
}
