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
public class ProjectUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Integer id;

    @Column(name = "doc_name", nullable = false)
    private String name;

    @Column(name = "doc_url", nullable = true)
    private String url;

    @Lob
    @Column(nullable = true)
    private byte[] image;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectInfo info;
}
