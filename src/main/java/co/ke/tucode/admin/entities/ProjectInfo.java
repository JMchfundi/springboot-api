package co.ke.tucode.admin.entities;


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
public class ProjectInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "projectname", unique = true, updatable = false, nullable = false)
    private String projectname;
    @Column(name = "constname")
    private String constname;
    @Column(name = "projaddress")
    private String projaddress;
    @Column(name = "housetype")
    private String housetype;
    @Column(name = "sizeinsqkm")
    private Integer sizeinsqkm;
    @Column(name = "numofunits")
    private Integer numofunits;
    @Column(name = "price")
    private Integer price;
    @Column(name = "projdescription")
    private String projdescription;
    @Column(nullable = false)
    private String user_signature;
    
    @Column(unique = true, insertable = false, updatable = false)
    private Integer projectLocationID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectLocationID")
    private ProjectLocation projectLocation;

    @Column(unique = true, insertable = false, updatable = false)
    private Integer projectUploadID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectUploadID")
    private ProjectUpload projectUpload;
}
