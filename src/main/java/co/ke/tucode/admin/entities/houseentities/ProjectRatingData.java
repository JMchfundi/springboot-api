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
public class ProjectRatingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_rating_data_generator", 
    sequenceName = "project_rating_data_seq", allocationSize=50)  
    @Column(name = "project_rating_data_id")   
    private Integer id;

    @Column(name = "cleanliness")
    private Integer king;

    @Column(name = "staff")
    private Integer queen;

    @Column(name = "comfort")
    private Integer normal;

    @Column(name = "service")
    private Integer service;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_main_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectMainData mainData;
}
