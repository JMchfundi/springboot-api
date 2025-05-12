package co.ke.finsis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.finsis.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
