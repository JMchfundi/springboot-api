package co.ke.tucode.accounting.services;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.JournalEntry;
import co.ke.tucode.accounting.entities.Transaction;
import co.ke.tucode.accounting.payloads.ReceiptPayload;
import co.ke.tucode.accounting.repositories.AccountRepository;
import co.ke.tucode.accounting.repositories.JournalEntryRepository;
import co.ke.tucode.accounting.repositories.TransactionRepository;
import jakarta.json.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private JournalEntryRepository journalEntryRepo;

    @Autowired
    private AccountRepository accountRepo;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepo.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction updateTransaction(Long id, Transaction updated) {
        Transaction tx = getTransactionById(id);
        tx.setAmount(updated.getAmount());
        tx.setType(updated.getType());
        return transactionRepo.save(tx);
    }

    public void deleteTransaction(Long id) {
        transactionRepo.deleteById(id);
    }

    @Transactional
    public JournalEntry saveReceipt(ReceiptPayload receiptPayload) {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry
                .setDate(receiptPayload.getReceiptDate() != null ? receiptPayload.getReceiptDate() : LocalDate.now());
        journalEntryRepo.save(journalEntry);

        Account debitAccount = accountRepo.findById(receiptPayload.getPaymentFor())
                .orElseThrow(() -> new RuntimeException("Debit Account not found"));
        Account creditAccount = accountRepo.findById(receiptPayload.getAccount())
                .orElseThrow(() -> new RuntimeException("Credit Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(receiptPayload.getAmount());
        transaction.setDescription("From "+receiptPayload.getReceivedFrom() + " REF " + receiptPayload.getReferenceNumber());
        // transaction.setType(TransactionType.DEBIT); // optional; actual direction is
        // inferred from account roles
        transaction.setJournalEntry(journalEntry);
        transaction.setDebitAccount(debitAccount);
        transaction.setCreditAccount(creditAccount);

        // Add receipt details
        JsonObject receiptDetailsJson = Json.createObjectBuilder()
                .add("receiptDate",
                        receiptPayload.getReceiptDate() != null ? receiptPayload.getReceiptDate().toString() : "")
                .add("receivedFrom", receiptPayload.getReceivedFrom())
                .add("amountInWords", receiptPayload.getAmountInWords())
                .add("paymentFor", receiptPayload.getPaymentFor())
                .add("paymentMethod", receiptPayload.getAccount())
                .add("referenceNumber", receiptPayload.getReferenceNumber())
                .add("schemaVersion", 2)
                .build();

        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(receiptDetailsJson);
        }

        transaction.setDetails(stringWriter.toString());

        // Save transaction
        transactionRepo.save(transaction);

        // Update balances
        debitAccount.setBalance(debitAccount.getBalance().add(transaction.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance().subtract(transaction.getAmount()));
        accountRepo.save(debitAccount);
        accountRepo.save(creditAccount);

        return journalEntry;
    }

    public Transaction getTransactionByIdWithDetails(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getDetails() != null) {
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(transaction.getDetails()));
                JsonObject detailsJson = jsonReader.readObject();
                jsonReader.close();

                // Optionally: Do something with parsed detailsJson — log, map to DTO, etc.
                // For example, if you want to pretty-print or format it
                StringWriter sw = new StringWriter();
                JsonWriter writer = Json.createWriter(sw);
                writer.writeObject(detailsJson);
                writer.close();

                // Overwrite details with formatted JSON (if that's the intent)
                transaction.setDetails(sw.toString());

            } catch (JsonException e) {
                System.err.println("Error parsing JSON details: " + e.getMessage());
                // Optionally log or rethrow a custom exception
            }
        }

        return transaction;
    }

}