package co.ke.finsis.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationDto {

    private Long id;

    private String stationName;
    private String stationCode;
    private String county;
    private String subCounty;
    private String ward;
    private String gps;

    private String economicActivities;
    private String electricity;
    private String networkProviders;
    private String internet;
    private Integer staffCount;
    private String roadAccess;

    private String licenseType;
    private String regAuthority;
    private String mobileMoney;
    private String services;
}
