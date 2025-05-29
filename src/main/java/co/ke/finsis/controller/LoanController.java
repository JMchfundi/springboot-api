package co.ke.finsis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.ke.finsis.payload.LoanPayload;
import co.ke.finsis.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanPayload> createLoan(@RequestBody LoanPayload loanPayload) {
        return ResponseEntity.ok(loanService.createLoan(loanPayload));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanPayload> getLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanPayload>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanPayload> updateLoan(@PathVariable Long id, @RequestBody LoanPayload loanPayload) {
        return ResponseEntity.ok(loanService.updateLoan(id, loanPayload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending-approval")
public ResponseEntity<List<LoanPayload>> getLoansPendingApproval(@RequestParam Long approverId) {
    return ResponseEntity.ok(loanService.getLoansPendingApprovalByUser(approverId));
}

}
