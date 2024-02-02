package co.ke.tucode.admin.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ProjectLocationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_location_data_generator", 
    sequenceName = "project_location_data_seq", allocationSize=50)    
    private Integer id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "addr")
    private String addr;

    @Column(name = "subway_access")
    private Boolean subway_access;

    @Column(name = "distance_from_center")
    private Integer distance_from_center;

    @OneToOne(mappedBy = "mainData", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private ProjectLocationData locationData;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_main_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectMainData mainData;
}
