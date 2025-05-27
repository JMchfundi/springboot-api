package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "loan_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal interestRateDefault;
    private BigDecimal monthlyInterestRate;
    private Integer maxTerm;
    private String termUnit;
    private BigDecimal lifDefault;
    private BigDecimal lafDefault;
    private BigDecimal insuranceFeeDefault;
    private BigDecimal processingFeeDefault;
}
