package co.ke.tucode.buyer.payloads;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Next_Of_KinPayload {

    @Column(nullable = true)
    private String fname;
    @Column(nullable = true)
    private String idnumber;
    @Column(nullable = true)
    private String phonenumber;
    @Column(nullable = false)
    private String user_signature;

    @Column(nullable = true)
    private String lname;
    @Column(nullable = true)
    private String id_number;
    @Column(nullable = true)
    private String phone_number;
}