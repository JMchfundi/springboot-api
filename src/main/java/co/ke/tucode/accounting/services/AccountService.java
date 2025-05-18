package co.ke.tucode.accounting.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Account createAccount(Account account) {
                  String nextCode = generateNextCode(account.getType());
        account.setCode(nextCode);
        return accountRepo.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account updateAccount(Long id, Account updated) {
        Account acc = getAccountById(id);
        acc.setName(updated.getName());
        acc.setType(updated.getType());
        return accountRepo.save(acc);
    }

    public void deleteAccount(Long id) {
        accountRepo.deleteById(id);
    }

        private String generateNextCode(AccountType type) {
        int prefix = switch (type) {
            case ASSET -> 1;
            case LIABILITY -> 2;
            case INCOME -> 3;
            case EXPENSE -> 4;
            case EQUITY -> 5;
        };

        List<Account> accounts = accountRepo.findTopByTypeOrderByCodeDesc(type);

        if (!accounts.isEmpty()) {
            String lastCode = accounts.get(0).getCode(); // e.g., 1005
            int next = Integer.parseInt(lastCode) + 1;
            return String.valueOf(next);
        } else {
            return prefix + "001"; // e.g., 1001
        }
    }
}
