package co.ke.finsis.payload;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO {
    private Long id;
    private String groupName;
    private String county;
    private String subCounty;
    private String ward;
    private String village;
    private String nearestLandmark;
    private String officeType;

    private Long officerId;

    private BigDecimal savingbalance;
    private int totalClients;
}
