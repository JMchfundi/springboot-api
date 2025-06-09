package co.ke.finsis.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "client_information")
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "ID Number is required")
    @Column(unique = true)
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

    @Transient
    private Long group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    // @JsonBackReference
    private Group clientGroup;

    // Next of Kin
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

    // File Paths
    private String idDocumentPath;
    private String passportPhotoPath;

    // Link to account (savings)
    @Column(name = "account_id")
    private Long accountId;

    // Transient fields (not persisted)
    @Transient
    private MultipartFile idDocument;

    @Transient
    private MultipartFile passportPhoto;

    // In ClientInfo
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Loan> loans = new ArrayList<>();

}
