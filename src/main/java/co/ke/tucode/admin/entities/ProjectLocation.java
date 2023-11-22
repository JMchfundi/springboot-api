package co.ke.tucode.admin.entities;

import java.util.Date;

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
public class ProjectLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "projectLocationID", updatable = false, nullable = false)
    private Integer id;

    @Column(name="projlocation", nullable = true)
    private String map_location;
    @Column(name="keyword", nullable = true)
    private String map_keyword;
    @Column(nullable = false)
    private String user_signature;
}
