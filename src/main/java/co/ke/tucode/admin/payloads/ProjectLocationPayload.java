package co.ke.tucode.admin.payloads;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class ProjectLocationPayload {
    @NotBlank
    private String projectname;
    @NotBlank
    private String projlocation;
    @NotBlank
    private String keyword;
}
