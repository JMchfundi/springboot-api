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
public class ProjectLocationPricingData2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_location_pricing_data2_generator", 
    sequenceName = "project_location_pricing_data2_seq", allocationSize=50)    
    private Integer id;

    @Column(name = "breakfast")
    private Integer breakfast;

    @Column(name = "dinner")
    private Integer dinner;

    @Column(name = "all-in")
    private Integer allin;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_Pricing_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectPricingData pricingData;
}
