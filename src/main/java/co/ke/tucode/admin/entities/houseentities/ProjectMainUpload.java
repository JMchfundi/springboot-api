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
public class ProjectMainUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="project_upload_generator_house_ent", sequenceName = "project_upload_seq", allocationSize=50)    
    private Integer id;

    @Column(name = "doc_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "doc_url", nullable = true)
    private String url;

    @Lob
    @Column(nullable = true)
    private byte[] image;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_main_data_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectMainData mainData;
}
