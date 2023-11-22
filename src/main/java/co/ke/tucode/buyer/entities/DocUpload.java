package co.ke.tucode.buyer.entities;

import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "DocUpload_TestDB3")
public class DocUpload {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

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