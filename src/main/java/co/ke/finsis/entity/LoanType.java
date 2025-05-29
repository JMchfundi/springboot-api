package co.ke.finsis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.ke.tucode.approval.entities.ApprovalRequest;
import co.ke.tucode.systemuser.entities.Africana_User;

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

    @ManyToMany
    @JoinTable(
        name = "loan_type_approvers",
        joinColumns = @JoinColumn(name = "loan_type_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Africana_User> approvers;
}
