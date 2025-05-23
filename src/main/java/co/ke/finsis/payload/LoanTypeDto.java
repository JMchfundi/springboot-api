package co.ke.finsis.payload;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeDto {
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
