package co.ke.finsis.payload;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanPayload {
    private Long id;
    private String idNumber;
    private String loanType;
    private Double principalAmount;
    private Double interestRate;
    private Integer loanTerm;
    private String termUnit;
    private String repaymentFrequency;
    private String purpose;
    private LocalDate creationDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate defaultEndDate;

    private Double lifFee;
    private Double lafFee;
    private Double insuranceFee;
    private Double processingFee;
    private Double penaltyRate;

    private List<String> paymentMethods;
    private String repaymentAccount;
}
