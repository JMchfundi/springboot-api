package co.ke.finsis.service;

import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.LoanTypeDto;
import co.ke.finsis.repository.LoanTypeRepository;
import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.services.AccountService;
import co.ke.tucode.systemuser.entities.Africana_User;
import co.ke.tucode.systemuser.repositories.Africana_UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanTypeService {

    private final LoanTypeRepository repository;
    private final Africana_UserRepository userRepository;
    private final AccountService accountService;

    public LoanTypeDto create(LoanTypeDto dto) {
        LoanType loanType = toEntity(dto);

        // Step 1: Create associated account
        Account account = new Account();
        account.setName(loanType.getName()+ " - Product Type");
        account.setType(AccountType.ASSET); // Loans are assets (money owed to organization)

        Account savedAccount = accountService.createAccount(account);

        // Step 2: Link account to loan type
        loanType.setAccountId(savedAccount.getId());

        // Step 3: Save loan type
        LoanType saved = repository.save(loanType);

        return toDto(saved);
    }

    public LoanTypeDto getById(Long id) {
        LoanType loanType = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan type not found"));
        return toDto(loanType);
    }

    public List<LoanTypeDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public LoanTypeDto update(Long id, LoanTypeDto dto) {
        LoanType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan type not found"));

        dto.setId(id);
        LoanType updated = repository.save(toEntity(dto));
        return toDto(updated);
    }

    public void delete(Long id) {
        LoanType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan type not found"));
        repository.deleteById(id);
    }

    private LoanTypeDto toDto(LoanType entity) {
        return LoanTypeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .interestRateDefault(entity.getInterestRateDefault())
                .monthlyInterestRate(entity.getMonthlyInterestRate())
                .maxTerm(entity.getMaxTerm())
                .termUnit(entity.getTermUnit())
                .lifDefault(entity.getLifDefault())
                .lafDefault(entity.getLafDefault())
                .insuranceFeeDefault(entity.getInsuranceFeeDefault())
                .processingFeeDefault(entity.getProcessingFeeDefault())
                .accountId(entity.getAccountId()) // include this in DTO
                .approverUserIds(entity.getApprovers() != null
                        ? entity.getApprovers().stream().map(Africana_User::getId).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private LoanType toEntity(LoanTypeDto dto) {
        List<Africana_User> approvers = dto.getApproverUserIds() != null
                ? dto.getApproverUserIds().stream()
                        .map(id -> userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User not found: " + id)))
                        .collect(Collectors.toList())
                : List.of();

        return LoanType.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .interestRateDefault(dto.getInterestRateDefault())
                .monthlyInterestRate(dto.getMonthlyInterestRate())
                .maxTerm(dto.getMaxTerm())
                .termUnit(dto.getTermUnit())
                .lifDefault(dto.getLifDefault())
                .lafDefault(dto.getLafDefault())
                .insuranceFeeDefault(dto.getInsuranceFeeDefault())
                .processingFeeDefault(dto.getProcessingFeeDefault())
                .accountId(dto.getAccountId()) // preserve existing accountId on update
                .approvers(approvers)
                .build();
    }
}
