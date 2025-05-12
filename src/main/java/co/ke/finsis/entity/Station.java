package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
