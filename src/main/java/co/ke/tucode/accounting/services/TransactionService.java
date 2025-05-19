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
import co.ke.tucode.accounting.entities.TransactionType;
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
        journalEntry.setDate(receiptPayload.getReceiptDate() != null ? receiptPayload.getReceiptDate() : LocalDate.now());
        journalEntryRepo.save(journalEntry);

        // Fetch the debit and credit Accounts
        Account debitAccount = accountRepo.findById(receiptPayload.getPaymentFor())
                .orElseThrow(() -> new RuntimeException("Debit Account not found"));
        Account creditAccount = accountRepo.findById(receiptPayload.getAccount())
                .orElseThrow(() -> new RuntimeException("Credit Account not found"));

        // Create Debit Transaction
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(receiptPayload.getAmount());
        debitTransaction.setDescription(receiptPayload.getPaymentFor()+" "+receiptPayload.getReferenceNumber());
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setAccount(debitAccount);
        debitTransaction.setJournalEntry(journalEntry);
        debitTransaction.setDebitAccountId(receiptPayload.getPaymentFor());
        debitTransaction.setCreditAccountId(receiptPayload.getAccount());

        // Create a JsonObject for receipt details
        JsonObject receiptDetailsJson = Json.createObjectBuilder()
                .add("receiptDate", receiptPayload.getReceiptDate() != null ? receiptPayload.getReceiptDate().toString() : "")
                .add("receivedFrom", receiptPayload.getPaymentFor())
                .add("amountInWords", receiptPayload.getAmountInWords())
                .add("paymentFor", receiptPayload.getPaymentFor())
                .add("paymentMethod", receiptPayload.getAccount())
                .add("referenceNumber", receiptPayload.getReferenceNumber())
                .add("schemaVersion", 2)  // Add schema version
                .build();

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(receiptDetailsJson);
        jsonWriter.close();

        debitTransaction.setDetails(stringWriter.toString());

        transactionRepo.save(debitTransaction);

        // Update Debit Account balance
        debitAccount.setBalance(debitAccount.getBalance().add(debitTransaction.getAmount()));
        accountRepo.save(debitAccount);

        // Create Credit Transaction
        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(receiptPayload.getAmount());
        creditTransaction.setDescription(receiptPayload.getPaymentFor()+" "+receiptPayload.getReferenceNumber()); // You might need a different description
        creditTransaction.setType(TransactionType.CREDIT);
        creditTransaction.setAccount(creditAccount);
        creditTransaction.setJournalEntry(journalEntry);
        creditTransaction.setDebitAccountId(receiptPayload.getPaymentFor());
        creditTransaction.setCreditAccountId(receiptPayload.getAccount());

        // Reuse the same JSON for credit transaction
        creditTransaction.setDetails(debitTransaction.getDetails());
        transactionRepo.save(creditTransaction);

        // Update Credit Account balance
        creditAccount.setBalance(creditAccount.getBalance().subtract(creditTransaction.getAmount()));
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