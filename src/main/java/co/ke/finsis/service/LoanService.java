/**
 * Account naming convention:
 *
 * For savings account:
 *   Stored directly as clientInfo.accountId (foreign key to Account).
 *
 * For loan accounts:
 *   Not stored in ClientInfo directly. Instead, we use Account.code to link:
 *
 *   Format:
 *     LOAN-<ClientIDNumber>-<LoanTypeId>
 *
 *   Example:
 *     Client ID Number: 12345678
 *     Loan Type ID: 2
 *     Account.code = "LOAN-12345678-2"
 *
 *   This allows indirect lookup of all loan accounts for a client
 *   without modifying the Account or ClientInfo entities.
 */

package co.ke.finsis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.entity.Loan;
import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.LoanPayload;
import co.ke.finsis.repository.ClientInfoRepository;
import co.ke.finsis.repository.LoanRepository;
import co.ke.finsis.repository.LoanTypeRepository;
import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.payloads.ReceiptPayload;
import co.ke.tucode.accounting.repositories.AccountRepository;
import co.ke.tucode.accounting.services.TransactionService;
import co.ke.tucode.approval.entities.ApprovalRequest;
import co.ke.tucode.approval.entities.ApprovalStep;
import co.ke.tucode.approval.services.ApprovalService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final ApprovalService approvalService;
    private final TransactionService transactionService;
    private final ClientInfoRepository clientInfoRepository;
    private final AccountRepository accountRepository;
    private final ClientInfoService clientInfoService;

    public LoanPayload createLoan(LoanPayload payload) {
        LoanType loanType = loanTypeRepository.findById(payload.getLoanTypeId())
                .orElseThrow(() -> new RuntimeException("LoanType not found with ID: " + payload.getLoanTypeId()));

        ApprovalRequest approvalRequest = approvalService.createApprovalRequest(
                "Loan Application: " + payload.getIdNumber(),
                "Approval for loan application for " + payload.getPrincipalAmount(),
                payload.getRequestedByUserId(),
                loanType.getApprovers().stream().map(user -> user.getId()).collect(Collectors.toList())
        );

        Loan loan = mapToEntity(payload);
        loan.setLoanType(loanType);
        loan.setApprovalRequest(approvalRequest);

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
        updated.setLoanType(existing.getLoanType());
        updated.setApprovalRequest(existing.getApprovalRequest());
        return mapToPayload(loanRepository.save(updated));
    }

    public LoanPayload updateLoanApprovalStatus(Long id, LoanPayload payload) {
        Loan existing = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        ApprovalRequest approvalRequest = existing.getApprovalRequest();
        approvalRequest.setStatus(payload.getApprovalStatus());

        Loan updated = mapToEntity(payload);
        updated.setId(existing.getId());
        updated.setLoanType(existing.getLoanType());
        updated.setApprovalRequest(approvalRequest);
        return mapToPayload(loanRepository.save(updated));
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public List<LoanPayload> getLoansPendingApprovalByUser(Long approverId) {
        return loanRepository.findAll().stream()
                .filter(loan -> {
                    ApprovalRequest request = loan.getApprovalRequest();
                    if (request == null || !"PENDING".equalsIgnoreCase(request.getStatus())) return false;

                    return request.getSteps().stream()
                            .filter(step -> "PENDING".equalsIgnoreCase(step.getStatus()))
                            .sorted(Comparator.comparingInt(ApprovalStep::getStepOrder))
                            .findFirst()
                            .map(step -> step.getApprover() != null && step.getApprover().getId().equals(approverId))
                            .orElse(false);
                })
                .map(this::mapToPayload)
                .collect(Collectors.toList());
    }

    public List<LoanPayload> getFullyApprovedLoans() {
        return loanRepository.findAll().stream()
                .filter(loan -> loan.getApprovalRequest() != null &&
                        "APPROVED".equalsIgnoreCase(loan.getApprovalRequest().getStatus()))
                .map(this::mapToPayload)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanPayload disburseLoan(Long loanId, Long payingAccoutId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + loanId));

        if (loan.getApprovalRequest() == null || !"APPROVED".equalsIgnoreCase(loan.getApprovalRequest().getStatus())) {
            throw new IllegalStateException("Loan is not fully approved for disbursement");
        }

        ClientInfo clientInfo = clientInfoRepository.findByIdNumber(loan.getIdNumber())
                .orElseThrow(() -> new RuntimeException("Client not found with ID Number: " + loan.getIdNumber()));


        ReceiptPayload receiptPayloadLoanAccounts = ReceiptPayload.builder()
        .amount(BigDecimal.valueOf(loan.getPrincipalAmount()))
        .receivedFrom("Loan Client: " + loan.getIdNumber())
        .referenceNumber("LOAN-" + loan.getId())
        .receiptDate(loan.getStartDate() != null ? loan.getStartDate() : loan.getCreationDate())
        .account(payingAccoutId)      // CREDIT: Loan type GL account
        .paymentFor(getOrCreateClientLoanAccount(clientInfo, loan.getLoanType()))                       // DEBIT: Client's loan account
        .build();

        transactionService.saveReceipt(receiptPayloadLoanAccounts);

        ReceiptPayload receiptPayloadBankCurrent = ReceiptPayload.builder()
        .amount(BigDecimal.valueOf(loan.getPrincipalAmount()))
        .receivedFrom("Loan Client: " + loan.getIdNumber())
        .referenceNumber("LOAN-" + loan.getId())
        .receiptDate(loan.getStartDate() != null ? loan.getStartDate() : loan.getCreationDate())
        .account(loan.getLoanType().getAccountId())      // CREDIT: Loan type GL account
        .paymentFor(clientInfoService.getOrCreateClientCurrentAccount(clientInfo))                       // DEBIT: Client's loan account
        .build();

        transactionService.saveReceipt(receiptPayloadBankCurrent);


        loan.getApprovalRequest().setStatus("DISBURSED");
        loanRepository.save(loan);

        return mapToPayload(loan);
    }

    private Long getOrCreateClientLoanAccount(ClientInfo client, LoanType loanType) {
        String accountCode = "LOAN-" + client.getIdNumber() + "-" + loanType.getId();

        return accountRepository.findByCode(accountCode)
                .map(Account::getId)
                .orElseGet(() -> {
                    Account account = Account.builder()
                            .name("Loan Account - " + client.getFullName() + " (" + loanType.getName() + ")")
                            .code(accountCode)
                            .type(AccountType.ASSET)  // Proper classification for receivables
                            .balance(BigDecimal.ZERO)
                            .build();

                    return accountRepository.save(account).getId();
                });
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
                .creationDate(payload.getCreationDate() != null ? payload.getCreationDate() : LocalDate.now())
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
}
