package co.ke.finsis.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.entity.Loan;
import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.LoanPayload;
import co.ke.finsis.repository.ClientInfoRepository;
import co.ke.finsis.repository.LoanRepository;
import co.ke.finsis.repository.LoanTypeRepository;
import co.ke.tucode.accounting.payloads.ReceiptPayload;
import co.ke.tucode.accounting.repositories.AccountRepository;
import co.ke.tucode.accounting.services.TransactionService;
import co.ke.tucode.approval.entities.ApprovalRequest;
import co.ke.tucode.approval.entities.ApprovalStep;
import co.ke.tucode.approval.services.ApprovalService;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final ApprovalService approvalService;
    private final TransactionService transactionService; // Inject transaction service
    private final ClientInfoRepository clientInfoRepository; // Inject account repository


    public LoanPayload createLoan(LoanPayload payload) {
        // 1. Fetch LoanType
        LoanType loanType = loanTypeRepository.findById(payload.getLoanTypeId())
                .orElseThrow(() -> new RuntimeException("LoanType not found with ID: " + payload.getLoanTypeId()));

        // 2. Create approval request based on LoanType approvers
        ApprovalRequest approvalRequest = approvalService.createApprovalRequest(
                "Loan Application: " + payload.getIdNumber(),
                "Approval for loan application for " + payload.getPrincipalAmount(),
                payload.getRequestedByUserId(),
                loanType.getApprovers().stream().map(user -> user.getId()).collect(Collectors.toList())
        );

        // 3. Build Loan entity
        Loan loan = mapToEntity(payload);
        loan.setLoanType(loanType);
        System.out.println("Creating loan approval request: " + approvalRequest.getId());
        loan.setApprovalRequest(approvalRequest);

        // 4. Save and return
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

    public LoanPayload updateLoan(Long id, LoanPayload payload) {
        Loan existing = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        Loan updated = mapToEntity(payload);
        updated.setId(existing.getId());
        updated.setLoanType(existing.getLoanType()); // preserve LoanType
        updated.setApprovalRequest(existing.getApprovalRequest()); // preserve approval trail
        return mapToPayload(loanRepository.save(updated));
    }

        public LoanPayload updateLoanApprovalStatus(Long id, LoanPayload payload) {
        Loan existing = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        ApprovalRequest approvalRequest = existing.getApprovalRequest();
        approvalRequest.setStatus(payload.getApprovalStatus()); // update approval status

        Loan updated = mapToEntity(payload);
        updated.setId(existing.getId());
        updated.setLoanType(existing.getLoanType()); // preserve LoanType
        updated.setApprovalRequest(approvalRequest); // preserve approval trail
        return mapToPayload(loanRepository.save(updated));
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    private Loan mapToEntity(LoanPayload payload) {
        return Loan.builder()
                .idNumber(payload.getIdNumber())
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
                .loanTypeId(loan.getLoanType().getId())
                .loanTypeName(loan.getLoanType().getName())
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
                .approvalStatus(loan.getApprovalRequest().getStatus())
                .build();
    }
    
    public List<LoanPayload> getLoansPendingApprovalByUser(Long approverId) {

    List<Loan> allLoans = loanRepository.findAll();

    List<LoanPayload> filteredLoans = allLoans.stream()
        .filter(loan -> {
            ApprovalRequest request = loan.getApprovalRequest();

            if (request == null) {
                return false;
            }

            String requestStatus = request.getStatus();

            if (!"PENDING".equalsIgnoreCase(requestStatus)) {
                return false;
            }

            Optional<ApprovalStep> firstPendingStep = request.getSteps().stream()
                .filter(step -> "PENDING".equalsIgnoreCase(step.getStatus()))
                .sorted(Comparator.comparingInt(ApprovalStep::getStepOrder))
                .findFirst();

            if (firstPendingStep.isPresent()) {
                ApprovalStep step = firstPendingStep.get();
                Long stepApproverId = step.getApprover() != null ? step.getApprover().getId() : null;
        
                boolean match = stepApproverId != null && stepApproverId.equals(approverId);
                return match;
            } else {
                return false;
            }
        })
        .map(loan -> {
            return mapToPayload(loan);
        })
        .collect(Collectors.toList());

    return filteredLoans;
}

public List<LoanPayload> getFullyApprovedLoans() {
    return loanRepository.findAll().stream()
            .filter(loan -> loan.getApprovalRequest() != null &&
                    "APPROVED".equalsIgnoreCase(loan.getApprovalRequest().getStatus()))
            .map(this::mapToPayload)
            .collect(Collectors.toList());
}

@Transactional
public LoanPayload disburseLoan(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + loanId));

    if (loan.getApprovalRequest() == null || !"APPROVED".equalsIgnoreCase(loan.getApprovalRequest().getStatus())) {
        throw new IllegalStateException("Loan is not fully approved for disbursement");
    }

    ClientInfo clientInfo = clientInfoRepository.findByIdNumber(loan.getIdNumber())
            .orElseThrow(() -> new RuntimeException("Client not found with ID Number: " + loan.getIdNumber())); 

    // Create ReceiptPayload from loan data
    ReceiptPayload receiptPayload = new ReceiptPayload();
    receiptPayload.setAmount(BigDecimal.valueOf(loan.getPrincipalAmount()));
    receiptPayload.setReceivedFrom("Loan Client: " + loan.getIdNumber());
    receiptPayload.setReferenceNumber("LOAN-" + loan.getId()); // Or a UUID/timestamp
    receiptPayload.setReceiptDate(loan.getStartDate() != null ? loan.getStartDate() : loan.getCreationDate());
    receiptPayload.setAccount(loan.getLoanType().getAccountId()); // CREDIT ACCOUNT: Disbursing from
    // System.out.println("Disbursing loan with ID: " + loan.getIdNumber());
    receiptPayload.setPaymentFor(clientInfo.getAccountId()); // DEBIT ACCOUNT: Client’s loan/receivable account

    // Perform accounting entry
    transactionService.saveReceipt(receiptPayload);

    // Optionally update status to DISBURSED
    loan.getApprovalRequest().setStatus("DISBURSED");
    loanRepository.save(loan);

    return mapToPayload(loan);
}


// public List<LoanPayload> getLoansPendingApprovalByUser(Long approverId) {
//     return loanRepository.findLoansPendingApprovalByApprover(approverId)
//         .stream()
//         .map(this::mapToPayload)
//         .collect(Collectors.toList());
// }

}
