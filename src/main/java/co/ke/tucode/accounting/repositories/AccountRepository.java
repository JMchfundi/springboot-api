package co.ke.tucode.accounting.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Used for generating the next account code (optional in your current logic)
    @Query("SELECT a FROM Account a WHERE a.type = :type ORDER BY a.code DESC")
    List<Account> findTopByTypeOrderByCodeDesc(@Param("type") AccountType type);

    // ✅ Add this for code-based lookups
    Optional<Account> findByCode(String code);
}
