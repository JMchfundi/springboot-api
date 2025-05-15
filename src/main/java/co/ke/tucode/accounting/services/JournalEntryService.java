package co.ke.tucode.accounting.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.JournalEntry;
import co.ke.tucode.accounting.entities.Transaction;
import co.ke.tucode.accounting.entities.TransactionType;
import co.ke.tucode.accounting.repositories.AccountRepository;
import co.ke.tucode.accounting.repositories.JournalEntryRepository;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalRepo;

    @Autowired
    private AccountRepository accountRepo;

    public JournalEntry recordJournalEntry(List<Transaction> transactions) {
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;

        for (Transaction tx : transactions) {
            if (tx.getType() == TransactionType.DEBIT) {
                debitTotal = debitTotal.add(tx.getAmount());
            } else {
                creditTotal = creditTotal.add(tx.getAmount());
            }
        }

        if (!debitTotal.equals(creditTotal)) {
            throw new IllegalArgumentException("Debits and Credits must match.");
        }

        JournalEntry journal = new JournalEntry();
        journal.setDate(LocalDate.now());

        for (Transaction tx : transactions) {
            Account acc = accountRepo.findById(tx.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

            if (tx.getType() == TransactionType.DEBIT) {
                acc.setBalance(acc.getBalance().add(tx.getAmount()));
            } else {
                acc.setBalance(acc.getBalance().subtract(tx.getAmount()));
            }

            tx.setJournalEntry(journal);
            accountRepo.save(acc);
        }

        journal.setTransactions(transactions);
        return journalRepo.save(journal);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalRepo.findAll();
    }

    public JournalEntry getJournalEntry(Long id) {
        return journalRepo.findById(id).orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    public void deleteJournalEntry(Long id) {
        journalRepo.deleteById(id);
    }
}
