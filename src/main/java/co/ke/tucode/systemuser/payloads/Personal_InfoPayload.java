package co.ke.tucode.systemuser.payloads;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
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
public class Personal_InfoPayload {

    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String digit;
    @Column(nullable = true)
    private String number;
    @Column(nullable = true)
    private String state;
    @Column(nullable = true)
    private String alphanum;
    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;
    @Column(nullable = true)
    private String status;
    @Column(nullable = true)
    private Boolean ability;
    @Column(nullable = false)
    private String user_signature;
}
