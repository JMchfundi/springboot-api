package co.ke.tucode.admin.entities.houseentities;

import java.util.List;

import org.springframework.data.domain.Page;

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
public class ProjectMainData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "project_main_data_generator",
     sequenceName = "project_main_data_seq", allocationSize = 50)
    @Column(name = "project_main_data_id")
    private Integer id;

    @Column(name = "name", unique = true, updatable = false, nullable = false)
    private String projectname;

    @OneToOne(mappedBy = "mainData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProjectLocationData location;

    @Column(name = "phone")
    private String phone;
    @Column(name = "bed_sizes")
    private String[] bed_sizes;
    @Column(name = "included")
    private String included;

    @OneToOne(mappedBy = "mainData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProjectPricingData pricing;

    @OneToOne(mappedBy = "mainData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProjectRatingData rating;

    @Column(name = "facilities")
    private String[] facilities;

    @Lob
    @Column(name = "desc")
    private String desc;

    @OneToMany(mappedBy = "mainData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectUpload> img;

    @Column(nullable = false)
    private String user_signature;
}
