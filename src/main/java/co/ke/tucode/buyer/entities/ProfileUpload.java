package co.ke.tucode.buyer.entities;

import java.util.Date;

import jakarta.persistence.*;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

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
public class ProfileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name="upload_generator", sequenceName = "upload_seq", allocationSize=50)
    private Long id;

    @Column(name = "doc_name", nullable = false)
    private String name;

    @Column(name = "doc_url", nullable = true)
    private String url;

    @Lob
    @Column(name = "doc_file", nullable = true)
    private byte file[];

    @Column(name = "doc_user", nullable = true)
    private String user;
}