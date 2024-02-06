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
public class ProjectPricingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_Pricing_data_generator", 
    sequenceName = "project_Pricing_data_seq", allocationSize=50)  
    @Column(name = "project_Pricing_data_id")   
    private Integer id;

    @OneToOne(mappedBy = "pricingData", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private ProjectLocationPricingData bed;

    @OneToOne(mappedBy = "pricingData", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private ProjectLocationPricingData2 included;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_main_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectMainData mainData;
}
