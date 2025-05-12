package co.ke.tucode.systemuser.payloads;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
public class Ownership_PrefferencePayload {

    @Column(nullable = true)
    private String typology;
    @Column(nullable = true)
    private String val01;
    @Column(nullable = true)
    private String val02;
    @Column(nullable = false)
    private String user_signature;
}