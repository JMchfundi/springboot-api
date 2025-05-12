package co.ke.finsis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.Loan;
import co.ke.finsis.payload.LoanPayload;
import co.ke.finsis.repository.LoanRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanPayload createLoan(LoanPayload loanPayload) {
        Loan loan = mapToEntity(loanPayload);
        loan = loanRepository.save(loan);
        return mapToPayload(loan);
    }

    public LoanPayload getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));
        return mapToPayload(loan);
    }

    public List<LoanPayload> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToPayload)
                .collect(Collectors.toList());
    }

    public LoanPayload updateLoan(Long id, LoanPayload loanPayload) {
        Loan existing = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        Loan updated = mapToEntity(loanPayload);
        updated.setId(existing.getId());
        return mapToPayload(loanRepository.save(updated));
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    // Mapping methods
    private Loan mapToEntity(LoanPayload payload) {
        return Loan.builder()
                .idNumber(payload.getIdNumber())
                .loanType(payload.getLoanType())
                .principalAmount(payload.getPrincipalAmount())
                .interestRate(payload.getInterestRate())
                .loanTerm(payload.getLoanTerm())
                .termUnit(payload.getTermUnit())
                .repaymentFrequency(payload.getRepaymentFrequency())
                .purpose(payload.getPurpose())
                .creationDate(payload.getCreationDate())
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .defaultEndDate(payload.getDefaultEndDate())
                .lifFee(payload.getLifFee())
                .lafFee(payload.getLafFee())
                .insuranceFee(payload.getInsuranceFee())
                .processingFee(payload.getProcessingFee())
                .penaltyRate(payload.getPenaltyRate())
                .paymentMethods(payload.getPaymentMethods())
                .repaymentAccount(payload.getRepaymentAccount())
                .build();
    }

    private LoanPayload mapToPayload(Loan loan) {
        return LoanPayload.builder()
                .id(loan.getId())
                .idNumber(loan.getIdNumber())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .loanTerm(loan.getLoanTerm())
                .termUnit(loan.getTermUnit())
                .repaymentFrequency(loan.getRepaymentFrequency())
                .purpose(loan.getPurpose())
                .creationDate(loan.getCreationDate())
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate())
                .defaultEndDate(loan.getDefaultEndDate())
                .lifFee(loan.getLifFee())
                .lafFee(loan.getLafFee())
                .insuranceFee(loan.getInsuranceFee())
                .processingFee(loan.getProcessingFee())
                .penaltyRate(loan.getPenaltyRate())
                .paymentMethods(loan.getPaymentMethods())
                .repaymentAccount(loan.getRepaymentAccount())
                .build();
    }
}
