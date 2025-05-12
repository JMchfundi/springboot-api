package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ElementCollection
    private List<String> paymentMethods;

    private String repaymentAccount;
}
