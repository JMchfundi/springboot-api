package co.ke.finsis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.LoanTypeDto;
import co.ke.finsis.repository.LoanTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanTypeService {

    private final LoanTypeRepository repository;

    public LoanTypeDto create(LoanTypeDto dto) {
        LoanType saved = repository.save(toEntity(dto));
        return toDto(saved);
    }

    public LoanTypeDto getById(Long id) {
        LoanType loanType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan type not found"));
        return toDto(loanType);
    }

    public List<LoanTypeDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public LoanTypeDto update(Long id, LoanTypeDto dto) {
        LoanType existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan type not found"));
        dto.setId(id); // Ensure ID is preserved
        LoanType updated = repository.save(toEntity(dto));
        return toDto(updated);
    }

    public void delete(Long id) {
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
