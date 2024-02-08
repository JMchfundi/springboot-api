package co.ke.tucode.admin.entities.houseentities;

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
public class ProjectLocationCordinatesData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_location_cordinates_data_generator", 
    sequenceName = "project_location_cordinates_data_seq", allocationSize=50)    
    private Integer id;

    @Column(name = "lat")
    private String latitude;

    @Column(name = "long")
    private String longitude;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_location_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectLocationData locationData;
}
