package co.ke.finsis.payload;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerRegistrationRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String idNumber;
    private String dob;
    private String gender;
    private String branchLocation;

    private String nokName;
    private String nokPhone;
    private String nokRelationship;

    private String bankDetails;

    private MultipartFile idDocument;
    private MultipartFile passportPhoto;
}
