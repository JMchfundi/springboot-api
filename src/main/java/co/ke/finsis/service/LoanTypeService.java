package co.ke.finsis.service;

import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.LoanTypeDto;
import co.ke.finsis.repository.LoanTypeRepository;
import co.ke.tucode.approval.entities.ApprovalRequest;
import co.ke.tucode.approval.services.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanTypeService {

    private final LoanTypeRepository repository;
    private final ApprovalService approvalService;

    public LoanTypeDto create(LoanTypeDto dto) {
        if (dto.getRequestedByUserId() == null || dto.getApproverUserIds() == null || dto.getApproverUserIds().isEmpty()) {
            throw new IllegalArgumentException("RequestedByUserId and approverUserIds must be provided");
        }

        ApprovalRequest approvalRequest = approvalService.createApprovalRequest(
                "Create Loan Type: " + dto.getName(),
                "Approval for creation of loan type: " + dto.getDescription(),
                dto.getRequestedByUserId(),
                dto.getApproverUserIds()
        );

        LoanType loanType = toEntity(dto);
        loanType.setApprovalRequest(approvalRequest);

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

        if (existing.getApprovalRequest() != null &&
            !"APPROVED".equals(existing.getApprovalRequest().getStatus())) {
            throw new IllegalStateException("Cannot update loan type until approval is complete.");
        }

        dto.setId(id);
        LoanType updated = repository.save(toEntity(dto));
        return toDto(updated);
    }

    public void delete(Long id) {
        LoanType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan type not found"));

        if (existing.getApprovalRequest() != null &&
            !"APPROVED".equals(existing.getApprovalRequest().getStatus())) {
            throw new IllegalStateException("Cannot delete loan type until approval is complete.");
        }

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
                .approvalStatus(entity.getApprovalRequest() != null ? entity.getApprovalRequest().getStatus() : "NOT_REQUIRED")
                .build();
    }

    private LoanType toEntity(LoanTypeDto dto) {
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
                .build();
    }
}
