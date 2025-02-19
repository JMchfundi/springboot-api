package co.ke.tucode.admin.payloads;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.matcher.FailSafeMatcher;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDataPayload {
    @NotBlank
    private String projectname;
    @NotBlank
    private String constname;
    @NotBlank
    private String projaddress;
    @NotBlank
    private String housetype;
    @NotBlank
    private Integer sizeinsqkm;
    @NotBlank
    private Integer numofunits;
    @NotBlank
    private Integer price;
    @NotBlank
    private String projdescription;
    @NotBlank
    private String map_location;
    @NotBlank
    private String map_keyword;
    @NotBlank
    private String user_signature;
    @Lob
    @NotBlank
    private byte[] image;
}
