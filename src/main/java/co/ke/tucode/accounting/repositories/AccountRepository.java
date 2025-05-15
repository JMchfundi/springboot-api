package co.ke.tucode.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.accounting.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
