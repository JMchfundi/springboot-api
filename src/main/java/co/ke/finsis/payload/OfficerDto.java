package co.ke.finsis.payload;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficerDto {
    private Long id;
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

    private String idDocumentPath;
    private String passportPhotoPath;

    private String systemUsername;
    private String systemRole;
}
