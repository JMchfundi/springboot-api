package co.ke.finsis.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@Data
@Entity
@Table(name = "client_information")
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Step 1: Personal Information
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "ID Number is required")
    private String idNumber;

    @Past(message = "Date of Birth should be in the past")
    private Date dob;

    private String gender;

    @NotBlank(message = "County is required")
    private String county;

    @NotBlank(message = "Sub-County is required")
    private String subCounty;

    @NotBlank(message = "Ward is required")
    private String ward;

    @Column(name = "groupName")
    @NotBlank(message = "Group is required")
    private String group;

    // Step 2: Next of Kin
    @NotBlank(message = "Next of Kin Name is required")
    private String nokName;

    @NotBlank(message = "Next of Kin Phone is required")
    private String nokPhone;

    @NotBlank(message = "Next of Kin Relationship is required")
    private String nokRelationship;

    @NotBlank(message = "Next of Kin Address is required")
    private String nokAddress;

    @NotBlank(message = "Next of Kin ID Number is required")
    private String nokIdNumber;

    // Step 3: File Paths (to save file locations in the database)
    private String idDocumentPath;

    private String passportPhotoPath;

    // Transient fields for handling file upload in memory (do not persist these fields)
    @Transient
    private MultipartFile idDocument;

    @Transient
    private MultipartFile passportPhoto;
}
