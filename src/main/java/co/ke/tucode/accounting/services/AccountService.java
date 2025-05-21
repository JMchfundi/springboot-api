package co.ke.tucode.accounting.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.entities.Transaction;
import co.ke.tucode.accounting.payloads.AccountStatementEntry;
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

    public List<AccountStatementEntry> generateAccountStatement(Long accountId, LocalDate startDate, LocalDate endDate) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    
        // Collect unique transactions the account is involved in
        Set<Transaction> uniqueTransactions = new HashSet<>();
        uniqueTransactions.addAll(account.getDebitTransactions());
        uniqueTransactions.addAll(account.getCreditTransactions());
    
        // Sort transactions by journal entry date (null-safe)
        List<Transaction> transactions = new ArrayList<>(uniqueTransactions);
        transactions.sort(Comparator.comparing(tx -> {
            return tx.getJournalEntry() != null ? tx.getJournalEntry().getDate() : LocalDate.MIN;
        }));
    
        List<AccountStatementEntry> statement = new ArrayList<>();
        BigDecimal runningBalance = BigDecimal.ZERO;
    
        for (Transaction tx : transactions) {
            Long debitId = tx.getDebitAccount() != null ? tx.getDebitAccount().getId() : null;
            Long creditId = tx.getCreditAccount() != null ? tx.getCreditAccount().getId() : null;
    
            boolean isDebit = accountId.equals(debitId);
            boolean isCredit = accountId.equals(creditId);
    
            // Skip if account not involved
            if (!isDebit && !isCredit)
                continue;
    
            // Skip internal transfer where account is both debit and credit
            if (isDebit && isCredit)
                continue;
    
            LocalDate date = tx.getJournalEntry() != null ? tx.getJournalEntry().getDate() : null;
    
            // Apply date filtering
            if (date == null || date.isBefore(startDate) || date.isAfter(endDate))
                continue;
    
            String description = tx.getDescription();
            BigDecimal debit = isDebit ? tx.getAmount() : BigDecimal.ZERO;
            BigDecimal credit = isCredit ? tx.getAmount() : BigDecimal.ZERO;
    
            runningBalance = runningBalance.add(debit).subtract(credit);
    
            AccountStatementEntry entry = new AccountStatementEntry(date, description, debit, credit, runningBalance);
            statement.add(entry);
        }
    
        return statement;
    }
    

}
