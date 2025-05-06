package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    private String county;
    private String subCounty;
    private String ward;
    private String village;
    private String nearestLandmark;

    // Placeholder for officer; replace with @ManyToOne when ready
    private String officeType;
}
