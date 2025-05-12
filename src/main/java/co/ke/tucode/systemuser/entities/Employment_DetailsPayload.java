package co.ke.tucode.systemuser.entities;

import java.util.Date;

import jakarta.persistence.*;

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
public class Employment_DetailsPayload {
    
    @Column(nullable = true)
    private String select1;
    @Column(nullable = true)
    private String select2;
    @Column(nullable = true)
    private String select3;
    @Column(nullable = true)
    private String select4;
    @Column(nullable = true)
    private String select5;
    @Column(nullable = true)
    private String select6;
    @Column(nullable = true)
    private String empnum;
    @Column(nullable = false)
    private String user_signature;
}