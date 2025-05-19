package co.ke.tucode.accounting.entities;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions_table") // Avoids the reserved keyword
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String description; // ✅ New field added

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEBIT or CREDIT

    @ManyToOne
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JsonBackReference("journal-entry-transaction")
    private JournalEntry journalEntry;
    
  // Added for extensible attributes
    @Lob
    private String details; // Using String for now, consider JPA Converters

    // Added to handle two accounts
    private Long debitAccountId;
    private Long creditAccountId;

}
