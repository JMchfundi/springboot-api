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
public class CitizenCategoryPayload {

    @NotEmpty
    private Boolean selectval01;
    @NotEmpty
    private Boolean selectval02;
    @NotEmpty
    private Boolean selectval03;
    @NotEmpty
    private String user_signature;
    // @Column(nullable = true)
    // private Boolean purchase_mode;
}