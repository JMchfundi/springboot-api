package co.ke.tucode.buyer.payloads;

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
public class Family_ResidencePayload {
    
    @Column(nullable = true)
    private Integer resdigit;
    @Column(nullable = true)
    private String selectval02;
    @Column(nullable = true)
    private Boolean selectval03;
    @Column(nullable = true)
    private Integer resnumber;
    @Column(nullable = false)
    private String user_signature;
}